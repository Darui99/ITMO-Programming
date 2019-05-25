(definterface Expression
  (^Number evaluate [match])
  (^String toString [])
  (diff [t])
  )

(defn evaluate [expr match] (.evaluate expr match))
(defn toString [expr] (.toString expr))
(defn diff [expr t] (.diff expr t))

(deftype Const [val]
  Expression
  (evaluate [this match] val)
  (toString [this] (str (format "%.1f" val)))
  (diff [this t] (Const. 0.0)))
(def Constant ->Const)
;(defn Constant [val] (Const. val))

(def ZERO (Constant 0.0))
(def ONE (Constant 1.0))

(deftype Var [name]
  Expression
  (evaluate [this match] (get match name))
  (toString [this] name)
  (diff [this t] (if (= name t) ONE ZERO))
  )
(def Variable ->Var)

(deftype AbstractOperation [f df stringOperation args]
  Expression
  (evaluate [this match] (apply f (map (fn [cur] (evaluate cur match)) args)))
  (toString [this] (str "(" stringOperation " " (clojure.string/join " " (map (fn [cur] (toString cur)) args)) ")"))
  (diff [this t] (apply df t args))
  )

(defn Add [& args] (AbstractOperation. + (fn [t & args] (apply Add (map (fn [cur] (diff cur t)) args))) "+" args))
(defn Subtract [& args] (AbstractOperation. - (fn [t & args] (apply Subtract (map (fn [cur] (diff cur t)) args))) "-" args))
(defn Multiply [& args] (AbstractOperation. *
                                            (fn [t & args] (if (= (count args) 1) (diff (first args) t)
                                                                                  (reduce (fn [res cur] (Add (Multiply (diff res t) cur) (Multiply res (diff cur t)))) args)))
                                            "*" args))
(defn Divide [& args] (AbstractOperation. (fn [& args] (/ (double (first args)) (double (apply * (rest args)))))
                                          (fn [t & args] (if (= (count args) 1) (diff (first args) t)
                                                                                (Divide (Subtract
                                                                                          (Multiply (diff (first args) t) (apply Multiply (rest args)))
                                                                                          (Multiply (first args) (diff (apply Multiply (rest args)) t)))
                                                                                        (Multiply (apply Multiply (rest args)) (apply Multiply (rest args))))))
                                          "/" args))
(defn Negate [& args] (AbstractOperation. - (fn [t & args] (apply Subtract (map (fn [cur] (diff cur t)) args))) "negate" args))
(declare Cosh) (declare Sinh)
(defn Sinh [& args] (AbstractOperation. (fn [& args] (Math/sinh (first args))) (fn [t & args] (Multiply (Cosh (first args)) (diff (first args) t))) "sinh" args))
(defn Cosh [& args] (AbstractOperation. (fn [& args] (Math/cosh (first args))) (fn [t & args] (Multiply (Sinh (first args)) (diff (first args) t))) "cosh" args))

(def operationsMap {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'sinh Sinh, 'cosh Cosh})
(defn getOperation [arg] (get operationsMap arg))

(defn recParse [expression] (cond
                              (number? expression) (Constant expression)
                              (seq? expression) (apply (getOperation (first expression)) (map recParse (rest expression)))
                              :default (Variable (str expression))))

(defn parseObject [s] (recParse (read-string s)))