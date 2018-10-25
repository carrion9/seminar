Functions based on Αναλυτική περιγραφή:
 * Views
   + Seminar
   + Trainee
   + Contractor
 * Lists
   + Seminars
   + Trainees
   + Contractors
 * 2 types of new Seminar (excel imports only)
   + Βήμα 2
 * View Trainees-Specialties-Seminar (?)
   + Βήμα 3
 * Import trainees (how?)
   + Βήμα 4
 * Print/export excel
   + Βήμα 4
   + Βήμα 6
   + Βήμα 7
   + Βήμα 10
   + Βήμα 11
   + Περίπτωση επανεξέτασης
 * Export to word (?)
   + Βήμα 5
 * Backend functionalities on import/export files
   + Βήμα 6
   + Περίπτωση επανεξέτασης
 * insert exam results & pass/fail
   + Βήμα 9
   + Τι θα ισχύει γενικά για την εφαρμογή
 * Add specialty to trainee on a seminar
   + Περίπτωση επανεξέτασης
 * Calculate/edit cost
   + Τι θα ισχύει γενικά για την εφαρμογή
 * Calculate/dropdown edit card status
   + Τι θα ισχύει γενικά για την εφαρμογή
 * Some colour shit
   + Επεξήγηση χρωμάτων για αποκωδικοποίηση των αρχείων EXCEL
   
*Nginx config:*
   
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
                try_files $uri /index.html;
        }

        location /api {
                proxy_pass http://seminar-api;
        }
}

