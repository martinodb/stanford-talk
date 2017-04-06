(ns stanford-talk.parser
  (:import (java.util Properties)
           (edu.stanford.nlp.pipeline StanfordCoreNLP Annotation)
           (edu.stanford.nlp.ling CoreAnnotations$SentencesAnnotation CoreAnnotations$TokensAnnotation
                                  CoreAnnotations$TextAnnotation CoreAnnotations$PartOfSpeechAnnotation
                                  CoreAnnotations$NamedEntityTagAnnotation CoreAnnotations$LemmaAnnotation)
           (edu.stanford.nlp.dcoref CorefCoreAnnotations$CorefChainAnnotation)
           (edu.stanford.nlp.sentiment SentimentCoreAnnotations$SentimentClass)))
           
           

(use '[clojure.repl :only (pst)])
(defmacro with-err-str
  "Evaluates exprs in a context in which *err* is bound to a fresh
  StringWriter.  Returns the string created by any nested printing
  calls."
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*err* s#]
       ~@body
       (str s#))))

(def stp-out (promise))
(def stp-err (promise))
(def pipeline (promise))


(def props (Properties.))
(.setProperty props "annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment")

;; AHA! pipeline is the first verbose function.
(deliver stp-err (with-err-str (deliver pipeline (StanfordCoreNLP. props))))
;;(deliver stp-out (with-out-str (deliver pipeline (StanfordCoreNLP. props))))


#_(defn ->text-data [tokens sent-num]
  (mapv (fn [t] {:sent-num sent-num
                :token (.get t CoreAnnotations$TextAnnotation)
                :pos  (.get t CoreAnnotations$PartOfSpeechAnnotation)
                :net  (.get t CoreAnnotations$NamedEntityTagAnnotation)
                :lemma  (.get t CoreAnnotations$LemmaAnnotation)
                :sentiment (.get t SentimentCoreAnnotations$SentimentClass)}) tokens))

#_(defn ->chain-refs [chain sentences]
 (mapv (fn [c] (let [mentions (.getMentionsInTextualOrder(.getValue c))]
                (mapv (fn [m] (let [set-num (dec (.sentNum m))
                                   tokens (.get (.get sentences set-num) CoreAnnotations$TokensAnnotation)
                                   m-start-index (dec (.startIndex m))
                                   m-end-index (dec (.endIndex m))
                                   token (.get tokens m-start-index)]
                               {:sent-num  set-num
                                :token  (.value token)
                                :gender (.toString (.gender m))
                                :mention-type (.toString (.mentionType m))
                                :number (.toString (.number m))
                                :animacy (.toString (.animacy m))}))
                      mentions)))
       (.entrySet chain)))


#_(defn process-text [text]
  (let [annotation (Annotation. text)
        _ (.annotate pipeline annotation)
        sentences (.get annotation CoreAnnotations$SentencesAnnotation)
        sentence-tokens (mapv (fn [s] (.get s CoreAnnotations$TokensAnnotation)) sentences)
        text-data (flatten (map-indexed (fn [i t] (->text-data t i)) sentence-tokens))
        chain-data (.get annotation CorefCoreAnnotations$CorefChainAnnotation)
        chain-refs (->chain-refs chain-data sentences)]
    {:token-data text-data
     :refs chain-refs}))
