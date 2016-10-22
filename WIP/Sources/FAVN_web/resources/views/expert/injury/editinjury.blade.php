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
	                    <label for="inputEmail3" class="col-sm-2 control-label">NAME :</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" value="A Bloody Nose">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label for="inputEmail3" class="col-sm-2 control-label">SYMPTOM :</label>
	                    <div class="col-sm-10">
	                        <textarea class="form-control" rows="3">A nosebleed occurs when blood vessels inside the nose break. A nosebleed occurs when blood vessels inside the nose break. A nosebleed occurs when blood vessels inside the nose break</textarea>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label for="inputEmail3" class="col-sm-2 control-label">INSTRUCTION :</label>
	                    <div class="col-sm-10">
	                        <textarea class="form-control" rows="3">Because they’re delicate, this can happen easily. Because they’re delicate, this can happen easily. Because they’re delicate, this can happen easily.</textarea>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label for="inputEmail3" class="col-sm-2 control-label">REFERENCES :</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" value="www.google.com">
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label for="inputEmail3" class="col-sm-2 control-label">PRIORITY :</label>
	                    <div class="col-sm-10">
	                        <input type="text" class="form-control" value="Hight">
	                    </div>
	                </div>
	            </form>
	            <div>
	                <div style="float: left; margin-left: 170px; margin-right: 20px;">
	                    <button class="btn btn-default" onclick="window.location.href='injury.html'">
	                        Back
	                    </button>
	                </div>
	                <div>
	                    <button class="btn btn-default" onclick="alert('Edit successfuly!')">
	                        Edit
	                    </button>
	                </div>
	            </div>
	        </div>
	    </div>   
	</div>
@endsection