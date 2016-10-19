@extends('expert.layout.index')

@section('content')
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <div class="header">
                            <h4 class="title">Profile</h4>
                        </div>
                        <div class="content">
                            <form>
                                <div class="row">
                                    <div class="col-md-5">
                                        <div class="form-group">
                                            <label>Specialized</label>
                                            <input type="text" class="form-control" disabled placeholder="Company" value="First Aid">
                                        </div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-group">
                                            <label>Username</label>
                                            <input type="text" class="form-control" placeholder="Username" value="HuongNhat">
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Email address</label>
                                            <input type="email" class="form-control" placeholder="Email" value="Huonnhat.net@gmail.com">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>First Name</label>
                                            <input type="text" class="form-control" placeholder="Company" value="Hướng">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Last Name</label>
                                            <input type="text" class="form-control" placeholder="Last Name" value="Nhật">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label>Address</label>
                                            <input type="text" class="form-control" placeholder="Home Address" value="Wherever NTH live ">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>City</label>
                                            <input type="text" class="form-control" placeholder="City" value="HaNoi">
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Country</label>
                                            <input type="text" class="form-control" placeholder="Country" value="Vietnam">
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Postal Code</label>
                                            <input type="number" class="form-control" placeholder="ZIP Code" value="100000">
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label>About Me</label>
                                            <textarea rows="5" class="form-control" placeholder="Here can be your description" value="Mike">Im a expert in First Aid. Experience of 10 years.. blabla</textarea>
                                        </div>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-info btn-fill pull-right">Update Profile</button>
                                <div class="clearfix"></div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-user">
                        <div class="image">
                            <img src="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400" alt="..."/>
                        </div>
                        <div class="content">
                            <div class="author">
                                <a href="#">
                                    <img class="avatar border-gray" src="assets/img/faces/hn.jpg" alt="..."/>

                                    <h4 class="title">Hướng Nhật<br />
                                        <small>huongnhat.net</small>
                                    </h4>
                                </a>
                            </div>
                            <p class="description text-center"> "Love the life you live <br>
                                Live the life you love <br>
                                Trust me"
                            </p>
                        </div>
                        <hr>
                        <div class="text-center">
                            <button href="#" class="btn btn-simple" onclick="window.location.href='https://www.facebook.com/kienmtse02994'"><i class="fa fa-facebook-square"></i></button>
                            <button href="#" class="btn btn-simple" onclick="window.location.href='https://www.facebook.com/kienmtse02994'"><i class="fa fa-twitter"></i></button>
                            <button href="#" class="btn btn-simple" onclick="window.location.href='https://www.facebook.com/kienmtse02994'"><i class="fa fa-google-plus-square"></i></button>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection