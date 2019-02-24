(ns clojurescript-react-three-renderer.prod
  (:require
    [clojurescript-react-three-renderer.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
