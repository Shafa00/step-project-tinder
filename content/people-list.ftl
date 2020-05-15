<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>People list</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-8 offset-2">
            <div class="panel panel-default user_panel">
                <div class="panel-heading">
                    <h3 class="panel-title">People you have liked</h3>
                </div>
                <div class="panel-body">
                    <div class="table-container" style="max-height: 500px; overflow: auto;">
                        <table class="table-users table" border="0">
                            <tbody>
                            <#list likedUsers as user>
                                <tr>
                                    <td width="10">
                                        <div class="avatar-img">
                                            <img class="img-circle" src="${user.image}" />  
                                        </div>

                                    </td>
                                    <td class="align-middle">
                                        <form method="post">
                                            <button type="submit" name="toMessage" value="${user.id}" class="btn btn-warning"> ${user.fullname} </button>
                                        </form>
                                    </td>
                                    <td class="align-middle">
                                        ${user.email}
                                    </td>
                                    <td  class="align-middle">
                                        Last Login:  ${user.lastLogin}
                                    </td>
                                </tr>

                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <a href="/users" class="btn btn-primary col-8 offset-2">Find out more users</a>
        <a href="/logout" class="btn btn-danger mt-1 col-8 offset-2">Log out</a>
    </div>
</div>

</body>
</html>