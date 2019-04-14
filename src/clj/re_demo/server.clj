(ns re-demo.server
  (:require [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            ;; For handler:
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :as response]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

;; Handler  was   initially  put   into  its  own   namespace  without
;; :gen-class.  Also this  file seems  to be  AOTed, cf.  project.clj,
;; whereas the original handler.clj was not ...
(defroutes routes
  (GET "/" [] (response/resource-response "index.html" {:root "public"}))
  (GET "/endpoint" [] (response/response "I am ok!"))
  (resources "/"))

;; This handler is referred to  in the project.clj as Figwheel handler
;; though it  does not seem  to process  requests for static  files in
;; "lein  figwheel" mode.   It is  called though  for things  like the
;; /endpoint route above ...
(def dev-handler (-> #'routes wrap-reload))

(def handler routes)

;; Server:
(defn -main [& args]
  (let [port (Integer/parseInt (or (env :port) "3000"))]
    (run-jetty handler {:port port :join? false})))
