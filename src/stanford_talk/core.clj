(ns stanford-talk.core
 ;; (:require [stanford-talk.parser :as stp])
  (:gen-class))

;; Stanford CoreNLP is very slow to load, and it sends log messages through stderr.
;; This ns has options to avoid loading it (and load a stub parser instead),
;; or to avoid those messages, 
;; without touching stanford-talk.parser, which is where the action happens.
;; You can use this ns as inspiration to do something similar.

;; This is a quick-and-dirty solution. Error messages are sent to /dev/null.
;; They should be logged to a file. But it works.



;; Uncomment one of these three to set the parse mode:
;;(def load-mode :none)
;;(def load-mode :silent)
(def load-mode :verbose)


  (case load-mode
  
    :none (do
     (println "I will just load a stub parser")
     (require '[stanford-talk.stub-parser :as stp])
     )
    :silent (do
      (println "will load, silent mode")
      (let [err System/err] 
         (System/setErr (java.io.PrintStream. (java.io.FileOutputStream. "/dev/null")))
         (require '[stanford-talk.parser :as stp])
         (System/setErr err)))
    :verbose (do 
      (println "will load, verbose mode")
      (require '[stanford-talk.parser :as stp])))

(def mytext "This sample text should be easy to annotate.")

(def stp-process-my-text (stp/process-text mytext))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  
  
  (case load-mode
  
    :none (do
     (println "not loading. Well, just loading a useless stub parser")
     (println (str "annotated text: "  stp-process-my-text )))
    :silent (do
      (println "loading, silent mode")
      (let [err System/err] 
         (System/setErr (java.io.PrintStream. (java.io.FileOutputStream. "/dev/null")))
         (println (str "annotated text: " stp-process-my-text ))
         (System/setErr err)))
    :verbose (do 
      (println "loading, verbose mode")
      (println (str "annotated text: "  stp-process-my-text )))))
