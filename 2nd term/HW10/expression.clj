;(ns check)

(definterface Expression
  (^Number evaluate [match])
  (^String toString [])
  (^String toStringSuffix [])
  (^String toStringInfix [])
  (diff [t])
  )

(defn evaluate [expr match] (.evaluate expr match))
(defn toString [expr] (.toString expr))
(defn toStringSuffix [expr] (.toStringSuffix expr))
(defn toStringInfix [expr] (.toStringInfix expr))
(defn diff [expr t] (.diff expr t))

(deftype Const [val]
  Expression
  (evaluate [this match] val)
  (toString [this] (str (format "%.1f" val)))
  (toStringSuffix [this] (str (format "%.1f" val)))
  (toStringInfix [this] (str (format "%.1f" val)))
  (diff [this t] (Const. 0.0)))
(def Constant ->Const)

(def ZERO (Constant 0.0))
(def ONE (Constant 1.0))
(def E (Constant (Math/E)))

(deftype Var [name]
  Expression
  (evaluate [this match] (get match name))
  (toString [this] name)
  (toStringSuffix [this] name)
  (toStringInfix [this] name)
  (diff [this t] (if (= name t) ONE ZERO))
  )
(def Variable ->Var)

(deftype AbstractOperation [f df stringOperation args]
  Expression
  (evaluate [this match] (apply f (map (fn [cur] (evaluate cur match)) args)))
  (toString [this] (str "(" stringOperation " " (clojure.string/join " " (map (fn [cur] (toString cur)) args)) ")"))
  (toStringSuffix [this] (str "(" (clojure.string/join " " (map (fn [cur] (toStringSuffix cur)) args)) " " stringOperation ")"))
  (toStringInfix [this] (if (= "negate" stringOperation) (str stringOperation "(" (toStringInfix (first args)) ")")
                                                         (str "(" (reduce (fn [res cur] (str res " " stringOperation " " (toStringInfix cur)))
                                                                          (toStringInfix (first args))
                                                                          (rest args)) ")")))
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
(defn Log [& args] (AbstractOperation. (fn [a b] (/ (Math/log (Math/abs b)) (Math/log (Math/abs a))))
                                       ()
                                       "//" args))
(defn Pow [& args] (AbstractOperation. (fn [& args] (Math/pow (first args) (second args)))  (fn [t & args] (diff (Pow E (Multiply (Log E (first args)) (rest args))) t)) "**" args))

(def operationsMap {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate})
(defn getOperation [arg] (get operationsMap arg))

(defn recParse [expression] (cond
                              (number? expression) (Constant expression)
                              (seq? expression) (apply (getOperation (first expression)) (map recParse (rest expression)))
                              :default (Variable (str expression))))

(defn parseObject [s] (recParse (read-string s)))

(defn parseExpression [expr] (cond (number? expr) (Constant expr) (symbol? expr) (Variable (str expr)) :else (apply (getOperation (first expr)) (map parseExpression (rest expr)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f]
  (fn [result]
    (if (-valid? result) (-return (f (-value result)) (-tail result)))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
          ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(def +parser _parser)
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or [p & ps]
  (reduce (partial _either) p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def *whitespace (+char " \t\n\r"))
(def *ws (+ignore (+star *whitespace)))

(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+seq (+opt (+char "-")) (+str (+plus *digit)) (+opt (+str (+seq (+char ".") (+plus *digit))))))))

(def *variable (+char "xyz"))
(def *arg (+or (+map Constant *number) (+map Variable (+map str *variable))))

(defn *str [s] (+ignore (apply +seq (map (fn [sym] (+char (str sym))) (seq s)))))

(declare *addsub)

(defn makeOperation [allowedOperations] (apply +or (map (fn [[key val]] (+seqf (constantly val) (*str key))) allowedOperations)))

(def *expr (+or *arg
                (+seqf (fn [op a] (op a)) (makeOperation {"negate" Negate}) *ws (delay *expr))
                (+seqn 1 (+char "(") *ws (delay *addsub) *ws (+char ")"))))

(defn *uni [operand allowedOperations folder] (+map folder (+seqf cons operand (+star (+seq *ws (makeOperation allowedOperations) *ws operand)))))

(defn *rfolder [a & other] (if (empty? other) a (let [[op w] (first other)] (op a (apply *rfolder w (rest other))))))
(defn *right [operand allowedOperations] (*uni operand allowedOperations (partial apply *rfolder)))

(def *lfolder (partial reduce (fn [a [op b]] (op a b))))
(defn *left [operand allowedOperations] (*uni operand allowedOperations *lfolder))

(def *powlog (*right *expr {"**" Pow "//" Log}))
(def *muldiv (*left *powlog {"*" Multiply "/" Divide}))
(def *addsub (*left *muldiv {"+" Add "-" Subtract}))

(def parseObjectInfix (+parser (+seqn 0 *ws *addsub *ws)))

;(def expr (Pow (Constant 2.0) (Constant 3.0)))
;(println (evaluate expr {"x" 0}))