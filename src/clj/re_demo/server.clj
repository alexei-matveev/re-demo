(ns re-demo.server
  (:require [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            ;; For handler:
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

;; Handler  was   initially  put   into  its  own   namespace  without
;; :gen-class.  Also this  file seems  to be  AOTed, cf.  project.clj,
;; whereas the original handler.clj was not ...
(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (resources "/"))

;; This  handler  ist  referred  to in  the  project.clj  as  Figwheel
;; handler:
(def dev-handler (-> #'routes wrap-reload))

(def handler routes)

;; Server:
(defn -main [& args]
  (let [port (Integer/parseInt (or (env :port) "3000"))]
    (run-jetty handler {:port port :join? false})))
