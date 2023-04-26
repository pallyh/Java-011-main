package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.data.dao.IUserDao;
import itstep.learning.data.entity.User;
import itstep.learning.service.HashService;
import itstep.learning.service.UploadService;
import itstep.learning.service.auth.AuthService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserProfileServlet extends HttpServlet {
    @Inject AuthService authService;
    @Inject private IUserDao userDao;
    @Inject private Logger logger;
    @Inject private UploadService uploadService;
    @Inject private HashService hashService;
    @Inject @Named("AvatarFolder") private String avatarFolder;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String profileUserLogin = req.getPathInfo();
        User profileUser = null;
        String viewName = "profile-404"; // профиль не найден
        if (profileUserLogin != null && profileUserLogin.length() > 1) {
            profileUserLogin = profileUserLogin.substring(1); // user
            profileUser = userDao.getUserProfile(profileUserLogin);
            if (profileUser != null) {
                User authUser = authService.getAuthUser();
                if (authUser != null && authUser.getId().equals(profileUser.getId())) {
                    // свой профиль
                    viewName = "profile-my";
                }
                else { // чужой профиль
                    req.setAttribute("profileUser", profileUser);
                    viewName = "profile";
                }
            }
        }
        req.setAttribute("viewName", viewName);
        req.getRequestDispatcher("../WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // String userName = req.getParameter("userName");
        // извлекаем тело запроса и переводим в строку - в JSON
        String body;
        try(InputStream bodyStream = req.getInputStream()) {
            byte[] buf = new byte[1024];
            ByteArrayOutputStream arr = new ByteArrayOutputStream();
            int len;
            while( (len = bodyStream.read(buf)) != -1) {
                arr.write(buf, 0, len);
            }
            body = arr.toString("UTF-8");
            arr.close();
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            resp.getWriter().print("Error");
            return;
        }

        try {
            JSONObject obj = new JSONObject(body);
            if(obj.has("userName")) {
                // запрос на изменение имени
                String userName = obj.optString("userName");
                // TODO: валидировать имя
                // проверяем авторизацию
                User user = authService.getAuthUser();
                if(user == null) {
                    resp.setStatus(401);
                    resp.getWriter().print("Unauthorized");
                    return;
                }
                user.setName(userName);
                if(userDao.updateName(user)) body = "OK";
                else body = "500";
            }
            if(obj.has("email")) {
                // запрос на изменение email
                String email = obj.optString("email");
                // TODO: валидировать email
                // проверяем авторизацию
                User user = authService.getAuthUser();
                if(user == null) {
                    resp.setStatus(401);
                    resp.getWriter().print("Unauthorized");
                    return;
                }
                user.setEmail(email);
                if(userDao.updateEmail(user)) body = "OK";
                else body = "500";
            }
        }
        catch (JSONException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            body = "Error";
        }
        resp.getWriter().print(body);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = authService.getAuthUser();
        if(user == null) {
            resp.setStatus(401);
            resp.getWriter().print("Unauthorized");
            return;
        }
        try {
            Map<String, FileItem> params = uploadService.parse(req);
            FileItem avatarItem = params.get("userAvatar");
            if(avatarItem == null) {
                resp.setStatus(400);
                resp.getWriter().print("Missed required field 'userAvatar'");
                return;
            }
            String path = req.getServletContext().getRealPath("/") + avatarFolder;
            String newAvatar = this.saveAvatar(avatarItem, path);
            if(newAvatar == null) {
                resp.setStatus(500);
                resp.getWriter().print("Error during upload");
                return;
            }
            // загрузка успешная, удаляем старый аватар
            String oldAvatar = user.getAvatar();
            if(oldAvatar != null) {
                File file = new File(path, oldAvatar);
                // System.out.println(file.getAbsolutePath());
                file.delete();
            }
            user.setAvatar(newAvatar);
            userDao.updateAvatar(user);
        }
        catch (FileUploadException ex) {
            resp.getWriter().print(ex.getMessage());
            return;
        }
        resp.getWriter().print("OK");
    }

    private String saveAvatar(FileItem avatar, String path) {
        // проверка и сохранение файла
        if (!avatar.isFormField()) { // это файловое поле
            if (avatar.getSize() > 0) { // есть данные
                String filename = avatar.getName();
                int dotPosition = filename.lastIndexOf('.');
                String extension = filename.substring(dotPosition);
                // Имя файла сохранить опасно - генерируем случайное имя и проверяем на существование файла
                String savedName = "";
                File file;
                do {
                    savedName = hashService.getHexHash(savedName + System.nanoTime()) + extension;
                    file = new File(path, savedName);
                } while (file.exists());
                try {
                    avatar.write(file);
                }
                catch (Exception ex) {
                    return null;
                }
                return savedName;
            }
        }
        return null;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getMethod().toUpperCase()) {
            case "GET":
            case "POST":
            case "PUT":
            case "HEAD":
            case "OPTIONS":
            case "DELETE":
            case "TRACE":
                super.service(req, resp);
                break;
            case "PATCH":
                doPatch(req, resp);
                break;
        }
    }
}

/*
Команды: пользователи могут состоять в разных командах
Задачи: задача ставится любым участником команды, вся команда явдяется исполнителем и "видит" чат
Чат: по каждой задаче свой чат, писать и просматривать могут только участники

CREATE TABLE `teams` (
    `id` char(36) NOT NULL COMMENT 'UUID',
    `name` varchar(64) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE `teams_users` (
    `id_team` char(36) NOT NULL,
    `id_user` char(36) NOT NULL,

    PRIMARY KEY (`id_team`, `id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE `tasks` (
    `id` char(36) NOT NULL COMMENT 'UUID',
    `name` varchar(64) NOT NULL,
    `status` int DEFAULT 0,
    `id_user` char(36) NOT NULL COMMENT 'Author',
    `id_team` char(36) NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `deadline` datetime,
    `priority` TINYINT default 0,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE `stories` (
    `id` char(36) NOT NULL COMMENT 'UUID',
    `id_user` char(36) NOT NULL COMMENT 'Author',
    `id_task` char(36) NOT NULL,
    `id_reply` char(36) NULL COMMENT 'Other story id',
    `content` TEXT NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

INSERT INTO `teams` VALUES('da3daa7f-c1c2-11ed-9caa-3c7c3fbb1a48', 'Frontend');
INSERT INTO `teams` VALUES('0c95ce09-c1c3-11ed-9caa-3c7c3fbb1a48', 'Backend');
INSERT INTO `teams` VALUES('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', 'Designer');

INSERT INTO `teams_users` VALUES ('da3daa7f-c1c2-11ed-9caa-3c7c3fbb1a48', 'f9001d31-b6a7-11ed-98a7-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('0c95ce09-c1c3-11ed-9caa-3c7c3fbb1a48', 'f9001d31-b6a7-11ed-98a7-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', 'f9001d31-b6a7-11ed-98a7-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('da3daa7f-c1c2-11ed-9caa-3c7c3fbb1a48', 'f9001d31-b6a7-11ed-98a7-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('0c95ce09-c1c3-11ed-9caa-3c7c3fbb1a48', 'f9001d31-b6a7-11ed-98a7-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', 'f9001d31-b6a7-11ed-98a7-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('da3daa7f-c1c2-11ed-9caa-3c7c3fbb1a48', 'e6c81ac5-c43a-11ed-a002-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', 'cc626c7e-c43a-11ed-a002-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('da3daa7f-c1c2-11ed-9caa-3c7c3fbb1a48', '94b69d4d-c43a-11ed-a002-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', '94b69d4d-c43a-11ed-a002-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('0c95ce09-c1c3-11ed-9caa-3c7c3fbb1a48', '73adb69f-c43a-11ed-a002-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', '1d465302-c43b-11ed-a002-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('0c95ce09-c1c3-11ed-9caa-3c7c3fbb1a48', '1d465302-c43b-11ed-a002-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('0c95ce09-c1c3-11ed-9caa-3c7c3fbb1a48', '0b5ad019-c43a-11ed-a002-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', '0b5ad019-c43a-11ed-a002-3c7c3fbb1a48');
INSERT INTO `teams_users` VALUES ('da3daa7f-c1c2-11ed-9caa-3c7c3fbb1a48', '0b5ad019-c43a-11ed-a002-3c7c3fbb1a48');

INSERT INTO `teams_users` VALUES ('290a7b7a-c1c3-11ed-9caa-3c7c3fbb1a48', '04d6467b-c43b-11ed-a002-3c7c3fbb1a48');

 */