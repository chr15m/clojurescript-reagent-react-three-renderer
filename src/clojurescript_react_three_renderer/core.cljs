(ns clojurescript-react-three-renderer.core
    (:require
      [reagent.core :as r]
      [cljsjs.react-three-renderer]))

(js/console.log js/React3)

;; -------------------------
;; Views

(defn animate [t]
  (reset! t (.getTime (js/Date.))))

(def d 20)

(defn component-scene []
  (let [position (THREE.Vector3. 0 0 5)
        t (r/atom 0)]
    (fn []
      (let [w (.-innerWidth js/window)
            h (.-innerHeight js/window)]
        [:> js/React3 {:mainCamera "camera"
                       :width w
                       :height h
                       :onAnimate (partial animate t)}
         [:scene
          [:perspectiveCamera {:name "camera"
                               :fov 75
                               :aspect (/ w h)
                               :near 0.1
                               :far 1000
                               :position position}]
          [:ambientLight {:color 0x505050 :intensity 0.5}]
          [:hemisphereLight {:skyColor 0xffffff
                             :groundColor 0xffffff
                             :intensity 0.2
                             :position (THREE.Vector3. 0 50 0)}]
          [:directionalLight {:color 0xffffff
                              :intensity 1
                              :castShadow true
                              :shadowMapWidth 1024
                              :shadowMapHeight 1024
                              :shadowCameraLeft (* -1 d)
                              :shadowCameraRight d
                              :shadowCameraTop d
                              :shadowCameraBottom (* -1 d)
                              :shadowCameraFar (* 3 d)
                              :shadowCameraNear d
                              :position (THREE.Vector3. d d d)
                              :lookAt (THREE.Vector3. 0 0 0)}]
          (doall
            (for [x (range (int (js/Math.abs (* 10 (js/Math.sin (* @t 0.001))))))]
              [:mesh {:key x
                      :rotation (THREE.Euler. 0 (* x 0.1) (js/Math.sin (* @t 0.001)))
                      :position (THREE.Vector3. 0 (* x 0.25) (* x 0.25))
                      :castShadow true
                      :receiveShadow true}
               [:boxGeometry {:width 0.5
                              :height 0.5
                              :depth 0.5}]
               [:meshLambertMaterial {:color 0x00ae50}]]))]]))))

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [component-scene] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
