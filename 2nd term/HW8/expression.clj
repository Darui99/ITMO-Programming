(defn operation [f] (fn [& args] (fn [match] (apply f (map (fn [cur] (cur match)) args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn [& args] (/ (double (first args)) (double (apply * (rest args)))))))
(def negate subtract)
(def med (operation (fn [& args] (nth (sort args) (quot (count args) 2)))))
(def avg (operation (fn [& args] (/ (apply + args) (count args)))))

(defn constant [val] (fn [match] val))
(defn variable [name] (fn [match] (get match name)))

(def operationsMap {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'avg avg, 'med med})
(defn getOperation [arg] (get operationsMap arg))

(defn recParse [expression] (cond
                              (number? expression) (constant expression)
                              (seq? expression) (apply (getOperation (first expression)) (map recParse (rest expression)))
                              :default (variable (str expression))))

(defn parseFunction [s] (recParse (read-string s)))