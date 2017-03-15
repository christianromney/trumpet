(ns trumpet.sound
  (:require [tonejs]
            [cljs.pprint :refer [pprint]]))

(defonce wahwah
  (.toMaster (js/Tone.AutoWah. 58.27 6 -30)))

(defonce synthesizer
  (.toMaster (js/Tone.AMSynth.)))

(defonce output
  (-> synthesizer
      (.connect wahwah)))

(def duration
  "Map of semantic duration names to values
  understood by the synthesizer."
  {:whole-note   "1n"
   :half-note    "2n"
   :quarter-note "4n"
   :eigth-note   "8n"})

(defn play
  "Plays a note in a given octave for the specified duration."
  ([octave-note]
   (play octave-note (duration :quarter-note)))

  ([octave-note time]
   (js/console.log "play: " (clj->js [octave-note time]))
   (.triggerAttackRelease output octave-note time)))

(defn octave
  "Converts a keyword note and integer octave into
  a string that is understood by the synthesizer.
  (octave :A 4) => 'A4'"
  [note n]
  (str (name note) n))
