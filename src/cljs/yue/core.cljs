(ns yue.core
  (:require [cljs.nodejs :as nodejs]
            [yue.ui :as ui]))

(def react-yue (nodejs/require "../src/js/react-yue/index"))
(def render (.-render react-yue))

(def gui (nodejs/require "gui"))

(def win (.create gui.Window #js {}))

(.setContentSize win #js {:width 400 :height 250})

(def content-view (.create gui.Container))

(.setStyle content-view #js {:flexDirection "column"})
(.setContentView win content-view)
(.center win)
(.activate win)
(.maximize win)

(render (ui/App) content-view)

(when-not (.. js/process -versions -yode)
  (.run gui.MessageLoop)
  (.exit js/process 0))