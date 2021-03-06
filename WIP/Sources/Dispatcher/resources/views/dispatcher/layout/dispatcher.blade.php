<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <link rel="icon" type="image/png" href="assets/img/markers/ic_115_center.png">
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

    <!-- jquery UI lib -->
    <link rel="stylesheet" type="text/css" href="assets/css/jquery-ui.min.css">

    <base href="{{ asset('') }}">


</head>
<body>
    @if(session('noti'))
           <input type="text" id="sessionNoti" name="sessionNoti" value="{{ session('noti') }}" hidden>
           <input type="text" id="sessionCaller" name="sessionCaller" value="{{ session('caller') }}" hidden>
    @endif
    @if(session('ambulance'))
        <input type="text" id="sessionAmbulance" name="sessionNoti" value="{{ session('ambulance') }}" hidden>
        <input type="text" id="sessionCaller" name="sessionCaller" value="{{ session('caller') }}" hidden>
    @endif

    <div class="wrapper">
        <!-- sidebar left -->
        @include('dispatcher.layout.sidebarleft')

        <div class="main-panel">
            <!-- navbar -->
             @include('dispatcher.layout.navbar')

            <div id="map"></div>

        </div>

        <!-- sidebar right -->
        @include('dispatcher.layout.sidebarright')
    </div>


</body>
<!-- include commons script -->
<script src="assets/js/commons/models.js" type="text/javascript"></script>
<script src="assets/js/commons/variables.js" type="text/javascript"></script>

<!-- include function script -->
<script src="assets/js/functions/googlemapHandle.js" type="text/javascript"></script>
<script src="assets/js/functions/dispatchHandle.js" type="text/javascript"></script>
<script src="assets/js/functions/domControl.js" type="text/javascript"></script>    


<!-- API -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcG5eSgX7ZSWWdvnnNUTz4tzNhYIK3uBs&language=vi&callback=initMap"
async defer></script> 
<script src="https://www.gstatic.com/firebasejs/3.6.1/firebase.js"></script>

<!--   Core JS Files   -->
<script src="assets/js/customizes/jquery-1.10.2.js" type="text/javascript"></script>
<script src="assets/js/customizes/bootstrap.min.js" type="text/javascript"></script>
<script src="assets/js/customizes/bootbox.min.js" type="text/javascript"></script>   
<script src="assets/js/customizes/bootstrap-checkbox-radio-switch.js"></script>
<script src="assets/js/customizes/chartist.min.js"></script>
<script src="assets/js/customizes/bootstrap-notify.js"></script>
<script src="assets/js/customizes/light-bootstrap-dashboard.js"></script>
<script src="assets/js/customizes/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="assets/js/customizes/jquery-ui.min.js" type="text/javascript"></script>


<!-- include function script -->
<script src="assets/js/functions/firebaseHandle.js" type="text/javascript"></script>




<!-- autocomplete search phone number -->
<script type="text/javascript">
    $('#phone').autocomplete({
        source : '{!!URL::route('autocompletePhone')!!}',
        minlenght : 1, 
        autoFocus : true,
        select : function(e, ui){
                // Assign value for caller object
                initNewMap();
                var tmpObj = ui.item;
                caller = ui.item;
                takingCaller = ui.item;
                tmpCallerPost = new google.maps.LatLng(caller.latitude, caller.longitude);
                callerPos = [];
                callerPos.push(tmpCallerPost);
                iniCallerForm(caller);
                clearAllMarkers();
                initAnCallerMarker(caller);
        }
    });
</script>
    

 <!-- Get list ambulance and update to firebase -->

     <script type="text/javascript">
         //callCanCelDispatcheService(2);
     </script>

     <script type="text/javascript">
      setUnAvailableAmbulanceNoti();
     </script>

     <!-- Handle dispatcher Click -->
     <script type="text/javascript">

     function onDispatchClick() {
        getReadyAmbulanceFromService();
        //$( "#form_caller" ).submit();
        // clearAllCurrentObject();
        // if(caller == null) {
        //  showAlertBox('Chưa khởi tạo trường hợp khẩn cấp');
        // } else {
        //  $( "#form_caller" ).submit();

        //  // -----------------
        //  // TODO :
        //  //getListAmbulanceAndDispatch();        
        // }
    }
    function getReadyAmbulanceFromService() {
        var caller_id = $('#caller_id').val();
        var symptom = $('#symptom').val();
        var dispatcher_user_id = "{{$userLogin->id}}"; 
        $.ajax({
         type:'GET',
         url:'getreadyambulance',
         data:{
             caller_id: caller_id,
             symptom: symptom,
             dispatcher_user_id: dispatcher_user_id
         },
         async: false,
         success:function(data){
             handleReturnDispatch(data);
         }
        });
    }
    </script>
<!--     <script type="text/javascript">
        DISPATCHER_ID = "{{$userLogin->id}}"; 

        getlistWaitingCaller(DISPATCHER_ID);
    </script> -->
</html>


