<!-- navbar -->
<nav class="navbar navbar-default navbar-fixed">
	<div class="container-fluid">
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					@if(isset($userLogin))
						Xin chào {{ $userLogin->name }}
					@endif
						<b class="caret"></b>
					</a>
					<ul class="dropdown-menu">
						<li><a href="logout">Đăng xuất</a></li>
					</ul>
				</li>

			</ul>
		</div>
	</div>
</nav>
<!-- end navbar -->