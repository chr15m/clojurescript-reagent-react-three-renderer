(ns ^:figwheel-no-load clojurescript-react-three-renderer.dev
  (:require
    [clojurescript-react-three-renderer.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
