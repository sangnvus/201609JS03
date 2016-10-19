@extends('expert.layout.index')

@section('content')
    <div class="content">
        <div class="card">
            <div class="content">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">ID :</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" value="1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">USER NAME :</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" value="Hướng Nhật">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">EMAIL :</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" value="huongnhat.net@gmail.com">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">Q & A TITLE :</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="3">Because they’re delicate, this can happen easily.</textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">Q & A CONTENT :</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="3">Because they’re delicate, this can happen easily. Because they’re delicate, this can happen easily. Because they’re delicate, this can happen easily.</textarea>
                        </div>
                    </div>

                </form>
                <hr>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">ANSWER :</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="3" placeholder="Write expert answer..."></textarea>
                        </div>
                    </div>
                </form>
                <div>
                    <div style="float: left; margin-left: 170px; margin-right: 20px;">
                        <button class="btn btn-default" onclick="window.location.href='q&a.html'">
                            Back
                        </button>
                    </div>
                    <div>
                        <button class="btn btn-default" onclick="alert('Answer successfuly!')">
                            Answer
                        </button>
                    </div>
                </div>

            </div>
        </div>
    </div> 
@endsection