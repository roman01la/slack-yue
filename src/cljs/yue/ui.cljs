(ns yue.ui
  (:require [cljs.nodejs :as nodejs]
            [yue.react]
            [rum.core :as rum]
            [sablono.core :refer [html]]
            [yue.styles :refer [draw-styles]]))

(defn Container [props & children]
  (let [{:keys [style]} props
        on-draw #(draw-styles style %2 %3)
        style (dissoc style :border-left :border-right :border-top :border-bottom :border)
        attrs (assoc props :on-draw on-draw :style style)]
    (html [:container attrs children])))

;; =============================================

(defn make-round-rect [p w h r s]
  (doto p
    (.moveTo #js {:x (+ s r) :y (+ s 0)})
    (.lineTo #js {:x (- w r s) :y (+ s 0)})
    (.bezierCurveTo #js {:x (- w r s) :y (+ s 0)}
                    #js {:x (- w s) :y (+ s 0)}
                    #js {:x (- w s) :y (+ s r)})
    (.lineTo #js {:x (- w s) :y (- h r s)})
    (.bezierCurveTo #js {:x (- w s) :y (- h r s)}
                    #js {:x (- w s) :y (- h s)}
                    #js {:x (- w r s) :y (- h s)})
    (.lineTo #js {:x (+ r s) :y (- h s)})
    (.bezierCurveTo #js {:x (+ r s) :y (- h s)}
                    #js {:x (+ s 0) :y (- h s)}
                    #js {:x (+ s 0) :y (- h r s)})
    (.lineTo #js {:x (+ s 0) :y (+ s r)})
    (.bezierCurveTo #js {:x (+ s 0) :y (+ s r)}
                    #js {:x (+ s 0) :y (+ s 0)}
                    #js {:x (+ s r) :y (+ s 0)})))

(defn draw-border-radius
  [{:keys [radius width color background-color]
    :or {width 0
         color "rgba(0,0,0,0.0)"}}
   p
   a]
  (let [w (.-width a)
        h (.-height a)
        r radius
        mk+ #(+ % (/ width 2))
        mk- #(- % (/ width 2))]
    (doto p
      (.setLineWidth width)
      (.setStrokeColor color)
      (.setFillColor background-color)
      (make-round-rect w h r 0)
      (.fill)
      (make-round-rect w h r (/ width 2))
      (.stroke))))

(rum/defc TeamIcon <
  rum/static
  []
  [:container {:on-draw #(draw-border-radius {:radius 5
                                              :background-color "#3D8DF7"}
                                             %2 %3)
               :style {:width 36
                       :height 36}}])

(rum/defc Sidebar <
  rum/static
  []
  (Container
   {:style {:width 288
            :flex-direction "row"
            :border-left {:color "#E8E8E8"
                          :width 1}}}
   (Container
    {:style {:width 68
             :padding 16
             :background-color "#261C25"
             :flex-direction "column"}}
    (TeamIcon))
   (Container
    {:style {:width 220
             :background-color "#4D394B"
             :flex-direction "column"}})))

(rum/defc ChatHeader <
  rum/static
  []
  (Container
   {:style {:height 80
            :border-bottom {:width 1
                            :color "#E5E5E5"}}}))

(rum/defc ChatInput <
  rum/static
  []
  (Container
   {:style {:height 64
            :padding-left 20
            :padding-right 20
            :padding-bottom 20}}
   (html
    [:container
     {:on-draw #(draw-border-radius {:radius 5
                                     :width 2
                                     :color "#717274"
                                     :background-color "#fff"}
                                    %2 %3)
      :style {:flex 1
              :flex-direction "row"
              :align-items "center"
              :padding-left 8
              :padding-right 8}}
     [:entry
      {:text "Message"
       :type "normal"
       :style {:width "100%"
               :height "100%"
               :color "#9F9F9F"
               :font-size 14}}]])))

(rum/defc ChatThread <
  rum/static
  []
  (Container
   {:style {:flex 1}}))

(rum/defc Main <
  rum/static
  []
  (Container
   {:style {:flex 1
            :flex-direction "column"}}
   (ChatHeader)
   (ChatThread)
   (ChatInput)))

(rum/defc App []
  (Container
   {:style {:flex 1
            :flex-direction "row"
            :background-color "#fff"}}
   (Sidebar)
   (Main)))