<%@ page import="itstep.learning.data.entity.User" %>
<%@ page import="itstep.learning.data.entity.Team" %>
<%@ page import="java.util.List" %>
<%@ page import="itstep.learning.data.entity.Task" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String domain = request.getContextPath() ;
    User authUser = (User) request.getAttribute( "authUser" ) ;
    List<Team> teams = (List<Team>) request.getAttribute( "teams" ) ;
    List<Task> tasks = (List< Task>) request.getAttribute( "tasks" ) ;
%>
<div class="row">

    <div class="col s5 m4 l3">
        <!-- region Блок задач -->
        <% for( Task task : tasks ) { %>
        <div class="row">
            <div class="col">
                <div class="card blue-grey darken-1">
                    <div class="card-content white-text">
                        <span class="card-title"><%= task.getName() %></span>
                        <p><%= task.getCreatedDt() %> -- <%= task.getDeadline() %></p>
                    </div>
                    <div class="card-action">
                        <a href="#<%= task.getId() %>">Discus</a>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
        <!-- endregion Конец блока задач -->
    </div>

    <div class="col s7 m8 l9">
        <!-- region Блок обсуждения (комментариев) -->
        <div id="chat"></div>
        <form method="post" id="story-form">
            <textarea id="textarea1" class="materialize-textarea" name="story-text"></textarea>
            <label for="textarea1">Textarea</label>
            <div class="row input-field right-align">
                <button class="btn waves-effect waves-teal" type="submit">отправить<i class="material-icons right">add</i></button>
            </div>
            <input type="hidden" name="story-id-task" />
        </form>
        <!-- endregion Блок обсуждения (комментариев) -->
    </div>

</div>


<!-- region Добавить задачу -->
<div class="row">
    <h4>Добавить задачу</h4>
    <form class="col s10 offset-s1 m8 offset-m2 l6 offset-l3"
          method="post" id="task-form">
        <div class="row input-field"><i class="material-icons prefix">content_paste</i>
            <input id="task-name" type="text" name="task-name">
            <label for="task-name">Название</label>
        </div>
        <div class="row input-field"><i class="material-icons prefix">people_outline</i>
            <select name="task-team">
                <option value="" disabled selected>Выберите команду</option>
                <% for( Team team : teams ) { %>
                <option value="<%= team.getId() %>"><%= team.getName() %></option>
                <% } %>
            </select>
            <label>Команда</label>
        </div>
        <div class="row input-field"><i class="material-icons prefix">event_available</i>
            <input id="task-deadline" type="text" class="datepicker" name="task-deadline">
            <label for="task-deadline">Завершение</label>
        </div>
        <div class="row input-field"><i class="material-icons prefix">priority_high</i>
            <select name="task-priority">
                <option value="" disabled selected>Выберите приоритет</option>
                <option value="0">Обычный</option>
                <option value="1">Высокий</option>
                <option value="2">Экстремальный</option>
            </select>
            <label>Приоритет</label>
        </div>
        <div class="row input-field right-align">
            <button class="btn waves-effect waves-teal" type="submit">создать<i class="material-icons right">add</i></button>
        </div>
    </form>
</div>
<!-- endregion -->
<button  class="btn waves-effect waves-teal" onclick="testEmail()"><i class="material-icons right">send</i>Test Email</button>

Д.З. Реализовать перевод GMT времени в сообщениях от сервера к локальному
времени клиента.
Реализовать на клиенте сортировку сообщений от сервера по убыванию времени


<script>
    function testEmail() {
        fetch( "<%= domain %>/story", {
            method: "PUT"
        }).then(r => r.text())
            .then( console.log ) ;
    }
    const tpl = "<div><i>{{moment}}</i>&emsp;<b>{{user}}</b>&emsp;<span>{{content}}</span></div>" ;

    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems, {});
        elems = document.querySelectorAll('.datepicker');
        instances = M.Datepicker.init(elems, {format: "yyyy-mm-dd"});
        initWebsocket() ;
        window.dispatchEvent( new Event("hashchange") ) ;
    });

    function sendClick() {
        window.websocket.send(
            document.getElementById("textarea2").value
        ) ;
    }
    function initWebsocket() {
        window.websocket = new WebSocket( `ws://${window.location.host}/WebBasics/chat` ) ;
        window.websocket.onopen = onWsOpen;
        window.websocket.onmessage = onWsMessage;
        window.websocket.onclose = onWsClose;
        window.websocket.onerror = onWsError;
    }
    function onWsOpen( e ) {
        // console.log( "onWsOpen", e ) ;
    }
    function onWsMessage( e ) {
        // console.log( "onWsMessage", e.data ) ;
        let model = JSON.parse( e.data ) ;
        if( typeof model.status !== 'undefined' ) {
            alert( "Message was not sent" ) ;
        }
        else {
            const chat = document.getElementById("chat") ;
            const html = htmlFromStoryModel( model ) ;
            chat.insertAdjacentHTML("beforeend", html) ;
        }
    }
    function onWsClose( e ) {
        // console.log( "onWsClose", e ) ;
    }
    function onWsError( e ) {
        console.log( "onWsError", e ) ;
    }

    document.addEventListener('submit', e => {
        e.preventDefault() ;
        switch( e.target.id ) {
            case 'story-form': sendStoryForm(); break ;
            case 'task-form' : sendTaskForm();  break ;
        }
    });

    function sendTaskForm() {
        fetch( window.location.href, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams( new FormData( document.querySelector("#task-form") ) )
        }).then( r=> r.text() )
            .then( t => {
                console.log(t);
                if(t === "OK") window.location.reload() ;
            } ) ;
    }

    function sendStoryForm() {  // версия с вебсокетом
        if( ! window.websocket ) throw 'websocket not init' ;

        const storyIdTask = document.querySelector('input[name="story-id-task"]') ;
        if( ! storyIdTask ) throw 'input[name="story-id-task"] not found' ;
        const taskId = storyIdTask.value ;  // TODO: Validate
        if( taskId.length < 36 ) {
            alert( "Select a task" ) ;
            return ;
        }

        const textarea = document.getElementById('textarea1') ;
        if( ! textarea ) throw 'textarea1 not found' ;
        const storyMessage = textarea.value ;  // TODO: Validate

        window.websocket.send(
            JSON.stringify({
                taskId: taskId,
                content: storyMessage
            }));

        textarea.value = "" ;
    }

    function sendStoryFormHttp() {
        fetch( '<%= domain + "/story" %>', {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams( new FormData( document.querySelector("#story-form") ) )
        }).then( r=> r.text() )
            .then( console.log ) ;
    }

    window.addEventListener('hashchange', () => {
        const taskId = window.location.hash.substring(1) ;
        if( ! /[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/.test(taskId)) return ;

        // TODO: проверить, что такая задача есть у пользователя

        document.querySelector('input[name="story-id-task"]').value = taskId ;
        // Получить список историй по этой задаче и отобразить
        fetch( "<%= domain %>/story?task-id=" + taskId )
            .then( r => r.json() )
            .then( j => {
                const chat = document.getElementById("chat");
                let chatHtml = "" ;
                let models = j.sort((a, b) => Date.parse(a.story.createdDt) - Date.parse(b.story.createdDt)) ;
                for( let model of models ) {
                    chatHtml += htmlFromStoryModel( model ) ;
                }
                chat.innerHTML = chatHtml ;
            });
    });

    function htmlFromStoryModel( model ) {
        return tpl.replace( "{{moment}}", utcDateToString( model.story.createdDt )  )
                  .replace( "{{user}}",    model.user.name )
                  .replace( "{{content}}", model.story.content ) ;
    }
    function utcDateToString( dateString ) {
        let dt = new Date( dateString + " UTC" ) ;
        let now = new Date() ;
        let timeString = dt.toTimeString().substring(0, 8) ;
        if( dt.toDateString() === now.toDateString() ) {  // сегодня - только время
            return timeString ;
        }
        else {  // и дата, и время
            return dt.toDateString() + ' ' + timeString ;
        }
    }
</script>