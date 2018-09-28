(ns yue.react
  (:require [cljs.nodejs :as nodejs]))

(set! (.. js/global -React) (nodejs/require "react"))