(ns yue.styles
  (:require-macros [yue.ui :refer [draw]]))

(defmulti draw-border (fn [dir style p a] dir))

(defmethod draw-border :left [_ {:keys [width color]} p a]
  (let [x (- (:width a) (/ width 2))]
    (draw p
          [:line-width width]
          [:stroke-color color]
          [:move-to #js {:x x :y 0}]
          [:line-to #js {:x x :y (:height a)}]
          [:stroke])))

(defmethod draw-border :right [_ {:keys [width color]} p a]
  (let [x (/ width 2)]
    (doto p
      (.setLineWidth width)
      (.setStrokeColor color)
      (.moveTo #js {:x x :y 0})
      (.lineTo #js {:x x :y (:height a)})
      (.stroke))))

(defmethod draw-border :top [_ {:keys [width color]} p a]
  (let [y (/ width 2)]
    (doto p
      (.setLineWidth width)
      (.setStrokeColor color)
      (.moveTo #js {:x 0 :y y})
      (.lineTo #js {:x (:width a) :y y})
      (.stroke))))

(defmethod draw-border :bottom [_ {:keys [width color]} p a]
  (let [y (- (:height a) (/ width 2))]
    (doto p
      (.setLineWidth width)
      (.setStrokeColor color)
      (.moveTo #js {:x 0 :y y})
      (.lineTo #js {:x (:width a) :y y})
      (.stroke))))

(defmethod draw-border :default [_ style p a]
  (draw-border :top style p a)
  (draw-border :right style p a)
  (draw-border :bottom style p a)
  (draw-border :left style p a))

;; =====================================

(defn draw-styles [style p a]
  (let [a {:width (.-width a)
           :height (.-height a)}]
    (doseq [[k v] style]
      (case k
        :border (draw-border :default (:border style) p a)
        :border-right (draw-border :right (:border-right style) p a)
        :border-top (draw-border :top (:border-top style) p a)
        :border-left (draw-border :left (:border-left style) p a)
        :border-bottom (draw-border :bottom (:border-bottom style) p a)
        nil))))