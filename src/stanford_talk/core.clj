(ns stanford-talk.core
 ;; (:require [stanford-talk.parser :as stp])
  (:gen-class))







(def load? true)
;;(def stp-out (promise))
;;(def stp-err (promise))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (if load? (do
  (println "loading")
  (require '[stanford-talk.parser :as stp])
  ;;(deliver stp-out (with-out-str (require '[stanford-talk.parser :as stp])))
  ;;(deliver stp-err (with-err-str (require '[stanford-talk.parser :as stp])))
  (println "done loading"))
  
  
  (do
  (println "not loading")
  ;;(deliver stp-out "meh")
  ;;(with-out-str (println "uh"))
  )))
