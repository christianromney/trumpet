(ns trumpet.db.mem
  "This namespace contains the in-memory application
  database in a Reagent atom."
  (:require [reagent.core :as reagent :refer [atom]]))

(def state
  "Stores all of the data values that control the behavior of the application."
  (atom {:scale :Cmaj
         :octave 3
         :instrument "B♭ Trumpet"
         :transposition 2}))
