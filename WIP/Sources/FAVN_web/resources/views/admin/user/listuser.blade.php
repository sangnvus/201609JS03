@extends('admin.layout.index')

@section('content')
     @if(session('ambulances'))
           <input type="text" id="ambulances" name="ambulances" value="{{ session('ambulances') }}" hidden>
     @endif

    <div class="content">
        <div class="container-fluid">
            <div class="row">

                <!--Table of injuries-->
                <div class="col-md-12">
                    <div class="card">
                        <div class="header">
                            <h4 class="title">
                                Người dùng
                                <a href="admin/user/adduser" class="btn btn-info btn-fill">Thêm người dùng</a>
                            </h4>
                            <p class="category">Phân loại người dùng theo</p>
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="stats">
                                        @if(isset($filterUser))
                                            <select id="filterUser" class="form-control" name="slcFilterUser">
                                                <option value="0"
                                                    @if($filterUser == 0)
                                                        selected
                                                    @endif
                                                >Tất cả</option>
                                                <option value="1"
                                                @if($filterUser == 1)
                                                    selected
                                                @endif
                                                >Quản trị viên</option>
                                                <option value="2"
                                                @if($filterUser == 2)
                                                    selected
                                                @endif
                                                >Chuyên gia</option>
                                                <option value="3"
                                                @if($filterUser == 3)
                                                    selected
                                                @endif
                                                >Điều phối viên</option>
                                                <option value="4"
                                                @if($filterUser == 4)
                                                    selected
                                                @endif
                                                >Đội trưởng kíp</option>

                                            </select>
                                        @else
                                            <select id="filterUser" class="form-control" name="slcFilterUser">
                                                <option value="0" selected>Tất cả</option>
                                                <option value="1">Quản trị viên</option>
                                                <option value="2">Chuyên gia</option>
                                                <option value="3">Điều phối viên</option>
                                                <option value="4">Đội trưởng kíp</option>
                                            </select>
                                        @endif
                                    </div>
                                </div>
                                <div class="col-md-9">
                                    <form action="admin/user/search" class="form-inline pull-right">
                                        <div class="form-group">
                                            <input type="text" class="form-control" id="search" placeholder="Search" name="term">
                                        </div>
                                        <button type="submit" class="btn btn-default btn-fill">Tìm kiếm</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <div class="content table-responsive table-full-width">

                            @if(session('noti'))
                                <div class="alert alert-success">
                                    {{ session('noti') }}
                                </div>
                            @endif

                            @if(isset($users))
                            <table class="table table-hover table-striped">
                                <thead>
                                    <th>Tài khoản</th>
                                    <th>Tên người dùng</th>
                                    <th>Số điện thoại</th>
                                    <th>Email</th>
                                    <th>Vai trò</th>
                                    <th>Thao tác</th>
                                </thead>
                                <tbody>
                                    <!-- show list of injury -->
                                    
                                        @foreach($users as $user) 
                                        <tr>
                                            <td>{{ $user->username }}</td> 
                                            
                                            <td>{{ $user->name }}</td>
                                
                                            <td>{{ $user->phone }}</td>

                                            <td>{{ $user->email }}</td>

                                            <td>
                                                @if($user->role_id == 1)
                                                    Quản trị viên
                                                @elseif($user->role_id == 2)
                                                    Chuyên gia
                                                @elseif($user->role_id == 3)
                                                    Điều phối viên
                                                @else
                                                    Đội trưởng kíp    
                                                @endif
                                            </td>
                                           
                                        
                                            <td style="width: 85px;">
                                                <button type="button" rel="tooltip" title="Edit Injury" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='admin/user/edituser/{{ $user->id }}'">
                                                    <i class="fa fa-edit fa-lg"></i>
                                                </button>

                                                <button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="callDelete({{ $user->id }})">
                                                    <i class="fa fa-times fa-lg"></i>
                                                </button>
                                            </td>
                                        </tr>
                                        @endforeach 
                                    <!-- end show list of injury -->
                                </tbody>
                            </table>
                            <!-- paging -->
                            <center><?php echo $users->render(); ?></center>
                            @endif
                        </div>

                       
                    </div>
                    <!--End table injuries-->

                </div>
            </div>
        </div>
    </div>
@endsection

@section('script')

    <!-- Get list ambulance and update to firebase -->
    @if(session('ambulances'))
    <script type="text/javascript">
        setAllAmbulanceToFirebase();
    </script> 
     @endif
        


    <!-- JS function confirm box when delete user -->
    <script type="text/javascript">

        function callDelete(id){
            bootbox.confirm({ 
                size: "small",
                message: "Bạn có muốn xóa chấn thương này ?", 
                callback: function(result){
                    if (result) {
                        window.location = 'admin/user/deleteuser/' + id;
                    }
                }
            })
        }

    </script>

    <!-- set class active for li (menu list item) -->
    <script type="text/javascript">
        document.getElementById("nav-users-item").className += "active";
    </script>


    <!-- Fillter user by dropdown list -->
    <script type="text/javascript">
        function redirect(goto){
            if (goto != '') {
                window.location = goto;
            }
        }

        var selectEl = document.getElementById('filterUser');

        selectEl.onchange = function(){
            var goto = 'admin/user/listuser/' + this.value;
            redirect(goto);
        };
    </script>
    
@endsection

