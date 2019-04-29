(defn isNumbers [args] (every? number? args))
(defn isVector [arg] (if (vector? arg) (isNumbers arg) false))
(defn isVectors [args] (every? isVector args))
(defn isMatrix [arg] (if (vector? arg) (isVectors arg) false))
(defn isMatrices [args] (every? isMatrix args))

(defn equalSizes [args] (reduce (fn [flag cur] (if (number? flag)
                                                 (if (== (count cur) flag) flag false)
                                                 flag))
                                (if (== (count args) 0) -1 (count (nth args 0))) args))

(defn containsNumber [arg] (some number? arg))
(defn vecChildren2rank [arg] (vec (reduce (fn [res cur] (concat res cur)) [] arg)))
(defn isTensor [arg] (cond
                       (isVector arg) true
                       (containsNumber arg) false
                       (and (equalSizes arg) (isTensor (vecChildren2rank arg))) true))

(defn isTensors [args] (every? isTensor args))

(defn multSc [f] (fn [x & scalars] {:pre [(isNumbers scalars)]} (mapv (fn [c] (f c (apply * scalars))) x)))

(def v*s (multSc *))
(def m*s (multSc v*s))
(def t*s (multSc (fn [t, s] (if (number? t) (* t s) (t*s t s)))))

(defn operation [fId f checkType] (fn [& args] {:pre [(equalSizes args) (checkType args)] } (if (== (count args) 1) (fId (nth args 0)) (reduce (fn [x y] (mapv f x y)) args))))

(def v+ (operation identity + isVectors))
(def v- (operation (fn [x] (v*s x -1)) - isVectors))
(def v* (operation identity * isVectors))
(def m+ (operation identity v+ isMatrices))
(def m- (operation (fn [x] (m*s x -1)) v- isMatrices))
(def m* (operation identity v* isMatrices))

(def t+ (operation identity (fn [x y] (if (number? x) (+ x y) (t+ x y))) isTensors))
(def t- (operation (fn [x] (t*s x -1)) (fn [x y] (if (number? x) (- x y) (t- x y))) isTensors))
(def t* (operation identity (fn [x y] (if (number? x) (* x y) (t* x y))) isTensors))

(defn vect [& args] {:pre [(equalSizes args) (isVectors args)] } (reduce (fn [x y] [(- (* (x 1) (y 2)) (* (x 2) (y 1))),
                                                                                    (- (* (x 2) (y 0)) (* (x 0) (y 2))),
                                                                                    (- (* (x 0) (y 1)) (* (x 1) (y 0)))])  args))

(defn scalar [x y] {:pre [(equalSizes [x y]) (isVectors [x y])] } (apply + (v* x y)))

(defn m*v [mat vec] {:pre [(isMatrix mat) (isVector vec)]} (mapv (fn [cv] (scalar cv vec)) mat))

(defn mergeXth [mat x] (reduce (fn [res cur] (conj res (cur x))) [] mat))

(defn transpose [mat] {:pre [(isMatrix mat) (isTensor mat)]} (reduce (fn [res cur] (conj res (mergeXth mat cur))) [] (range (count (mat 0)))))

(defn m*m [& args] {:pre [(isMatrices args)]} (reduce (fn [x y] (mapv (fn [cv] (m*v (transpose y) cv)) x)) args))