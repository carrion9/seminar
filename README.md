 
*Nginx config:*

```Nginx
upstream seminar-webpack {
        server 127.0.0.1:3000;
         keepalive 64;
}

upstream seminar-api {
        server 127.0.0.1:5000;
}

server {
        listen 80 ;
        server_name 178.128.203.233;
        root /home/juan/seminar/frontend/build;

        location / {
                proxy_pass         http://seminar-webpack;
                proxy_redirect     off;
                proxy_set_header   X-Real-IP $remote_addr;
                proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header   X-Forwarded-Host $server_name;
                try_files          $uri /index.html;
        }

        location /api {
                proxy_pass http://seminar-api;
        }
}
```

