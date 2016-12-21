<nav id="navbar" class="navbar navbar-default navbar-fixed">
    <div id='divNoti' style="display: none;"></div>
    <div id="divNavContent" class="nav-content" style="float:right;">
    <input id="userLogin" type="hidden">
	    @if(isset($userLogin))
	    <input id="userLogin" type="hidden" value="{{$userLogin->id}}">
						Điều phối viên {{ $userLogin->name }} |
		@endif
	    <a href="javascript:void(0)" onclick="confirmLogout();">Đăng xuất</a>
    </div>
</nav>



