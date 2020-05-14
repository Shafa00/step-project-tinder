<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Signup Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/style.css">
</head>

<body class="text-center">
<form method="post" class="form-signin" enctype="multipart/form-data">
    <img class="mb-4" src="https://getbootstrap.com/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
    <input type="email" name="email" id="inputEmail" class="form-control" placeholder="Email address" required
           autofocus>
    <input type="text" name="fullname" id="inputEmail" class="form-control" placeholder="Full name" required autofocus>
    <input type="password" name="password" id="inputfn" class="form-control" placeholder="Password" required autofocus>
    <input type="password" name="confirmed" id="inputPassword" class="form-control" placeholder="Confirm password"
           required>
    <input type="file" name="img" id="inputPassword" class="form-control-file mb-1" required>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
    <p class="mt-5 mb-3 text-muted">&copy; Tinder 2018</p>
</form>
</body>
</html>