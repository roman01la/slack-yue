(ns yue.ui)

(defmulti compile-form first)

(defmethod compile-form :line-width [[_ value]]
  `(.setLineWidth ~value))

(defmethod compile-form :stroke-color [[_ value]]
  `(.setStrokeColor ~value))

(defmethod compile-form :move-to [[_ value]]
  `(.moveTo ~value))

(defmethod compile-form :line-to [[_ value]]
  `(.lineTo ~value))

(defmethod compile-form :stroke [_]
  `(.stroke))

(defmacro draw [p & forms]
  (let [forms (map compile-form forms)]
    `(doto ~p ~@forms)))