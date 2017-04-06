(ns stanford-talk.core
 ;; (:require [stanford-talk.parser :as stp])
  (:gen-class))







(def load-mode :silent)


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  
  #_(if load? (do
  (println "loading")
  (require '[stanford-talk.parser :as stp])

  (println "done loading"))
  
  
  (do
  (println "not loading")
  ))
  
  (case load-mode
  
    :none (do
     (println "not loading"))
    :silent (do
      (println "loading, silent mode")
      (let [err System/err] 
         (System/setErr (java.io.PrintStream. (java.io.FileOutputStream. "/dev/null")))
         (require '[stanford-talk.parser :as stp])
         (System/setErr err)))
    :verbose (do 
      (println "loading, verbose mode")
      (require '[stanford-talk.parser :as stp]))))
