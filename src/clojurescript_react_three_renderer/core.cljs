(ns clojurescript-react-three-renderer.core
    (:require
      [reagent.core :as r]
      [cljsjs.react-three-renderer]))

(js/console.log js/React3)
(js/console.log js/THREE)

;; -------------------------
;; Views

(defn animate [t]
  (reset! t (.getTime (js/Date.))))

(def d 20)

(defn component-box [t]
  (fn []
    [:mesh {:position (THREE.Vector3. 0 (+ 1.5 (js/Math.sin (* @t 0.005))) 0)
            :rotation (THREE.Euler. 0 0 0)
            :castShadow true}
     [:boxGeometry {:width 0.5
                    :height 0.5
                    :depth 0.5
                    }]
     [:meshLambertMaterial {:color 0xaaaaaa}]]))

(defn component-boxes [t]
  (fn []
    [:object3D
     (doall
       (for [x (range (int (js/Math.abs (* 10 (js/Math.sin (* @t 0.001))))))]
         [:mesh {:key x
                 :rotation (THREE.Euler. (* x -0.2) (* x 0.1) (js/Math.sin (* @t 0.001)))
                 :position (THREE.Vector3. -1 (* x 0.3) 0)
                 :castShadow true
                 :receiveShadow true}
          [:boxGeometry {:width 0.5
                         :height 0.5
                         :depth 0.5}]
          [:meshLambertMaterial {:color 0x00ae50}]]))]))

(defn component-camera [w h t]
  (fn []
    [:perspectiveCamera
     {:name "camera"
      :fov 75
      :aspect (/ w h)
      :near 0.1
      :far 1000
      :position (THREE.Vector3. (* (js/Math.sin (* @t 0.001)) 3) 2 (* (js/Math.cos (* @t 0.001)) 3))
      :lookAt (THREE.Vector3. 0 1 0)}]))

(defn component-scene []
  (let [t (r/atom 0)]
    (fn []
      (let [w (.-innerWidth js/window)
            h (.-innerHeight js/window)]
        [:> js/React3 {:mainCamera "camera"
                       :width w
                       :height h
                       :onAnimate (partial animate t)
                       :clearColor 0xffffff
                       :shadowMapEnabled true
                       :shadowMapType THREE.PCFShadowMap
                       :shadowMapSoft true
                       :antialias true}
         [:scene
          [component-camera w h t]
          [:ambientLight {:color 0x505050 :intensity 0.8}]
          [:hemisphereLight {:skyColor 0xffffff
                             :groundColor 0x444444
                             :intensity 0.9
                             :position (THREE.Vector3. 0 50 0)}]
          [:directionalLight {:color 0xffaaaa
                              :intensity 0.2
                              :castShadow true
                              :shadowMapWidth 2048
                              :shadowMapHeight 2048
                              :shadowCameraLeft (* -1 d)
                              :shadowCameraRight d
                              :shadowCameraTop d
                              :shadowCameraBottom (* -1 d)
                              :shadowCameraFar (* 3 d)
                              :shadowCameraNear 1.5
                              :shadowCameraFov 75
                              :shadowBias -0.00022
                              :position (THREE.Vector3. d (/ d 2) d)
                              :lookAt (THREE.Vector3. 0 0 0)}]
          #_ [:pointLight {:color 0xffffff
                        :intensity 0.2
                        :castShadow true
                        :shadowRadius 8
                        :position (THREE.Vector3. 1 5 1)}]
          [component-box t]
          [component-boxes t]
          [:mesh {:position (THREE.Vector3. 0 0 0)
                  :rotation (THREE.Euler. (/ js/Math.PI -2) 0 0)
                  :receiveShadow true}
           [:planeBufferGeometry {:width 10000
                                  :height 10000}]
           [:meshLambertMaterial {:color 0xffffff}]]]]))))

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [component-scene] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
