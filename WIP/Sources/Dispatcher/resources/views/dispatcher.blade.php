<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <link rel="icon" type="image/png" href="assets/img/favicon.ico">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <title>Dispatcher</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />


    <!-- Bootstrap core CSS     -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" />

    <!-- Animation library for notifications   -->
    <link href="assets/css/animate.min.css" rel="stylesheet"/>

    <!--  Light Bootstrap Table core CSS    -->
    <link href="assets/css/dashboard.css" rel="stylesheet"/>


    <!--  CSS for google map -->
    <link href="assets/css/googlemap.css" rel="stylesheet" />

    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />

    <base href="{{ asset('') }}">


</head>
<body>
    <div class="wrapper">
        <div class="sidebar" style="margin-right: 5px" data-color="azure">

            <!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


            <div class="sidebar-wrapper">
                <div class="logo">
                    <a href="dispatcher.html" class="simple-text">
                        Điều phối
                    </a>
                </div>

                <ul class="nav">
                    <li style="padding: 10px;">
                        <div>
                            <input type="text" class="form-control" placeholder="Tên nạn nhân">
                        </div>
                    </li>
                    <li style="padding: 10px;">
                        <div>
                            <input type="text" class="form-control" placeholder="Số điện thoại nạn nhân">
                        </div>
                    </li>
                    <li style="padding: 10px;">
                        <div>
                            <input type="text" class="form-control" placeholder="Địa chỉ nạn nhân">
                        </div>
                    </li>

                    <li style="padding: 10px;">
                        <div>
                            <input type="text" class="form-control" name="injury" list="injury" placeholder="Injury...">       
                        </div>
                        <datalist id="injury">
                            <option>Nose blood</option>
                            <option>Burn</option>
                            <option>Hear attach</option>
                            <option>Nose blood</option>
                            <option>Burn</option>
                            <option>Hear attach</option>
                        </datalist>
                    </li>
                    <li style="padding: 10px;">
                        <div style="align-content: center;">
                            <button style="background-color: green; color: white; float: right; width: 100px" type="button" class="btn btn-default">Điều phối xe</button>
                            <button style="background-color: red; color: white; float: right; width: 100px" type="button" class="btn btn-default">Hủy</button>
                        </div>
                    </li>

                </ul>

            </div>
        </div>

        <div class="main-panel">
            <nav class="navbar navbar-default navbar-fixed">
                <div class="container-fluid">
                    <div class="collapse navbar-collapse">
                        <ul class="nav navbar-nav navbar-left">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <i class="fa fa-globe"></i>
                                    <b class="caret"></b>
                                    <span class="notification">5</span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Notification 1</a></li>
                                    <li><a href="#">Notification 2</a></li>
                                    <li><a href="#">Notification 3</a></li>
                                    <li><a href="#">Notification 4</a></li>
                                    <li><a href="#">Another notification</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="">
                                    <i class="fa fa-search"></i>
                                </a>
                            </li>
                        </ul>

                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <a href="#">
                                    Log out
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <div id="map"></div>

        </div>

        <div class="sidebar" style="margin-left: 5px;" data-color="azure">
            <div class="sidebar-wrapper">
                <div class="container">
                    <h3>Trường hợp cấp cứu</h3>
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="wating.html">Waiting</a></li>
                        <li><a href="taking.html">Taking</a></li>
                    </ul>
                </div>
                <div style="">
                <ul class="nav">
                <li class="active">
                    <a href="dashboard.html">
                        <p>Caller : 0350 987785</p>
                        <p>Address : Nguyễn Chí Thanh</p>
                    </a>
                </li>
                <li class="active">
                    <a href="dashboard.html">
                        <p>Caller : 0350 987785</p>
                        <p>Address : Nguyễn Chí Thanh</p>
                    </a>
                </li>
                <li class="active">
                    <a href="dashboard.html">
                        <p>Caller : 0350 987785</p>
                        <p>Address : Nguyễn Chí Thanh</p>
                    </a>
                </li>
                <li class="active">
                    <a href="dashboard.html">
                        <p>Caller : 0350 987785</p>
                        <p>Address : Nguyễn Chí Thanh</p>
                    </a>
                </li>
                </ul>
                </div>

            </div>
        </div>
    </div>


</body>

<!--   Core JS Files   -->
<script src="assets/js/jquery-1.10.2.js" type="text/javascript"></script>
<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>

<!--  Checkbox, Radio & Switch Plugins -->
<script src="assets/js/bootstrap-checkbox-radio-switch.js"></script>

<!--  Charts Plugin -->
<script src="assets/js/chartist.min.js"></script>

<!--  Notifications Plugin    -->
<script src="assets/js/bootstrap-notify.js"></script>

<!--  Google Maps Plugin    -->
<script type="text/javascript" src="assets/js/googlemap.js"></script>

<!-- Light Bootstrap Table Core javascript and methods for Demo purpose -->
<script src="assets/js/light-bootstrap-dashboard.js"></script>

<!-- Light Bootstrap Table DEMO methods, don't include it in your project! -->
<script src="assets/js/demo.js"></script>


<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCXKxXg5C-3GiOZuWIhhLJyniiHna9IT04&callback=initMap"
    async defer></script>
</html>
