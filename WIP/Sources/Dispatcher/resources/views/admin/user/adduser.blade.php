@extends('admin.layout.index')
@section('content')
	<div class="content">
		<div class="container-fluid">
			<div class="row">

					<!-- Addnew injury form -->
                    <div class="col-md-12">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Thêm người dùng</h4>
                            </div>
                            <div class="content">

                                <!-- show errors and noti -->
                                @if(count($errors) > 0)
                                    <div class="alert alert-danger">
                                        @foreach($errors->all() as $err)
                                            {{ $err }}<br/>
                                        @endforeach
                                    </div>
                                @endif

                                @if(session('noti'))
                                <div class="alert alert-success">
                                    {{ session('noti') }}
                                </div>
                                @endif
                                
                       

                                <form action="admin/user/adduser" method="POST">
                                    <input type="hidden" name="_token" value="{{ csrf_token() }}"/>
                                    <div class="row">
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <label>Tên người dùng</label>
                                                <input type="text" class="form-control" placeholder="Tên người dùng..." name="name">
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <label>Vai trò</label>
                                            <div class="stats">
                                                <select id="filterRole" class="form-control" name="role">
                                                    @if(isset($roles))
                                                        @foreach($roles as $role)
                                                            <option value="{{ $role->id }}">{{ $role->role_name }}</option>
                                                        @endforeach
                                                    @endif
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <label>Trung tâm</label>
                                            <div class="stats">
                                                <select id="filterCenter" class="form-control" name="center" disabled>
                                                    @if(isset($centers))
                                                        @foreach($centers as $center)
                                                            <option value="{{ $center->id }}">{{ $center->center_name }}</option>
                                                        @endforeach
                                                    @endif
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <label>Đội</label>
                                                <input id="team" type="text" class="form-control" placeholder="Đội..." name="team" disabled>
                                            </div>
                                        </div>
                                        
                                    </div>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Số điện thoại</label>
                                                <input type="text" class="form-control" placeholder="Số điện thoại..." name="phone">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Email</label>
                                                <input type="text" class="form-control" placeholder="Email..." name="email">
                                            </div>
                                        </div>
                                    </div>


                                    <div class="row">
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label>Địa chỉ</label>
                                                <textarea class="form-control" placeholder="Địa chỉ..." name="address"></textarea>
                                            </div>
                                        </div>
                                    </div>
                        

                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Tên đăng nhập</label>
                                                <input type="text" class="form-control" placeholder="Tên đăng nhập..." name="username">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Mật khẩu</label>
                                                <input type="password" class="form-control" placeholder="Mật khẩu..." name="password">
                                            </div>
                                        </div>
                                    </div>
                                    

                                    <div class="row">
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <input type="submit" class="form-control btn btn-fill" value="Thêm người dùng">
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="form-group">
                                               <input type="reset" class="form-control btn btn-fill" value="Làm mới">
                                            </div>
                                        </div>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- End addnew injury form -->
					
			</div>
		</div>
	</div>
@endsection

@section('script')

    <!-- Role chose even -->
    <script type="text/javascript">


        var selectEl = document.getElementById('filterRole');
        selectEl.onchange = function(){
            if (selectEl.value == '3') { 
                document.getElementById('team').disabled = true;
                document.getElementById('filterCenter').disabled = false;
            } else if(selectEl.value == '4') { // Ambulance captain
                document.getElementById('team').disabled = false;
                document.getElementById('filterCenter').disabled = false;
            } else{
                document.getElementById('team').disabled = true;
                document.getElementById('filterCenter').disabled = true;
            }
        };

    </script>

    


@endsection