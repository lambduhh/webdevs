(ns ljm.webdevs
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [clojure.core.async :as a]))

(defn greet [req]
  (cond
    (= "/" (:uri req))
    {:status  200
     :body    "howdy planetary creatures!! "
     :headers {}}
    (= "/goodbye" (:uri req))
    {:status  200
     :body    "Goodbye, Cruel World!!"
     :headers {}}
    :else
    {:status  404
     :body    "Whoopsy Daisy! Page not found : ( "
     :headers {}}))


(defonce server (atom nil))

(defn make-server!
  ([] (make-server! 3000))
  ([port]
   (println (str "[+] Creating server on " (Integer. port)))
   (let [svr (jetty/run-jetty greet {:port  (Integer. port)
                                     :join? false})]
     ;; next we set the value of the server atom to be a
     ;; function which will stop the server
     ;; so you use it with
     ;; (@server)
     ;; that will stop the server
     (println (str "[+] Server created"))

     (reset! server (fn stop! [] (.stop svr))))))

(defn stop-server! []
  (println "[+] Stopping server!")
  (when @server
    (@server)))

(defn restart-server!
  ([] (restart-server! 3000))
  ([port]
   (stop-server!)
   (Thread/sleep 1000)
   (make-server! port)))




(defn -main [port]
  (restart-server! port))

(restart-server!)
