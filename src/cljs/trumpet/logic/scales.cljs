(ns trumpet.logic.scales
  "Information about musical notes and scales."
  (:require [cljs.pprint :refer [pprint]]))

(def note-names
  "These are the names of all the notes, arranged
  starting with C."
  [:C :C# :D :D# :E :F :F# :G :G# :A :A# :B])

(def octaves
  "We support a range of 8 octaves"
  8)

(def notes
  "The full range of notes across all octaves"
  (vec
   (flatten
    (for [o (range (inc octaves))]
      (for [n note-names]
        (keyword (str (name n) o)))))))

(def major-scale-indeces
  "A major scale begins with the tonic, and proceeds
  with the pattern 2 2 1 2 2 2 1 where 2 represents
  two half-steps (or a whole step) and 1 represents
  a single half-step."
  [0 2 4 5 7 9 11 12])

(def minor-scale-indeces
  "A minor scale begins with the tonic, and proceeds
  with the pattern 2 1 2 2 1 2 2 2  where 2 represents
  two half-steps (or a whole step) and 1 represents
  a single half-step."
  [0 2 3 5 7 8 10 12])

(def key-offsets
  "Mapping from a note name to its relative position in the list
  of notes."
  {:C  0
   :C# 1
   :D  2
   :D# 3
   :E  4
   :F  5
   :F# 6
   :G  7
   :G# 8
   :A  9
   :A# 10
   :B  11})

(defn- scale
  "Calculates the notes of a scale given a key, octave,
  transposition, and scale pattern."
  [key octave transpose scale-pattern]
  (let [octave-start (* octave (count key-offsets))
        key-start    (+ octave-start (key-offsets key) transpose)
        shifted      (drop key-start notes)]
    (mapv #(nth shifted %) scale-pattern)))

(defn major-scale
  "Given a keyword key and an integer octave, returns a vector of keyword notes
  containing the octave-notes in the corresponding major scale."
  [key octave transpose]
  (scale key octave transpose major-scale-indeces))

(defn minor-scale
  "Given a keyword key and an integer octave, returns a vector of keyword notes
  containing the octave-notes in the corresponding minor scale."
  [key octave transpose]
  (scale key octave transpose minor-scale-indeces))

(def scale-type->fn
  {:maj major-scale
   :min minor-scale})

(defn octave-note->note
  "Remove digits from the string representation of a note"
  [note]
  (keyword (apply str (re-seq #"\D" (name note)))))
