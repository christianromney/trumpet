(ns trumpet.app
  "This namespace is the main entrypoint of the application
  It renders the main application component which is the root
  of the UI component tree and binds it to a DOM node."
  (:require [reagent.core :as reagent]
            [trumpet.db.mem :as db]
            [trumpet.ui.components :as components]))

(enable-console-print!)

;; -- entrypoint --

(defn init []
  (reagent/render-component
   [components/main db/state]
   (js/document.getElementById "container")))
