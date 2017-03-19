(ns trumpet.logic.instruments
  "Contains a listing of band instruments and their
  transpositions.")

(def concert-pitch
  "These intruments are not transposing and
  play as written."
  0)

(def +major-second
  "A major second (2 half-steps) higher than concert pitch.
  Also known as B♭ transposition."
  2)

(def transpositions
  {;; Concert Pitch Instruments
   "Piccolo"              concert-pitch
   "Xylophone"            concert-pitch
   "Flute"                concert-pitch
   "Oboe"                 concert-pitch
   "Bassoon"              concert-pitch
   "Trombone"             concert-pitch
   "Baritone"             concert-pitch
   "Euphonium"            concert-pitch
   "Tuba"                 concert-pitch

   ;; B♭ Transposing Instruments
   "B♭ Clarinet"          +major-second
   "Bass Clarinet"        +major-second
   "Contrabass Clarinet"  +major-second
   "B♭ Trumpet"           +major-second
   "B♭ Coronet"           +major-second
   "Tenor Sax"            +major-second
   "Soprano Sax"          +major-second
   })

(def fingering
  {"B♭ Trumpet"
   {:Ab "⚪⚫⚫"
    :A  "⚫⚫⚪"
    :A# "⚫⚪⚪"
    :Bb "⚫⚪⚪"
    :B  "⚪⚫⚪"
    :C  "⚪⚪⚪"
    :C# "⚫⚫⚫"
    :Db "⚫⚫⚫"
    :D  "⚫⚪⚫"
    :D# "⚪⚫⚫"
    :Eb "⚪⚫⚫"
    :E  "⚫⚫⚪"
    :F  "⚫⚪⚪"
    :F# "⚪⚫⚪"
    :Gb "⚪⚫⚪"
    :G  "⚪⚪⚪"
    :G# "⚪⚫⚫"}})
