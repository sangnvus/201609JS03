@extends('expert.layout.index')
@section('content')
	<div class="content">
		<div class="container-fluid">
			<div class="row">

					<!-- Addnew injury form -->
                    <div class="col-md-12">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Thêm chấn thương</h4>
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
                                
                       

                                <form action="expert/injury/addinjury" method="POST" enctype="multipart/form-data">
                                    <input type="hidden" name="_token" value="{{ csrf_token() }}"/>
                                    <div class="row">
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label>Tên chấn thương</label>
                                                <input type="text" class="form-control" placeholder="Chấn thương..." name="injury_name">
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <label>Ưu tiên</label>
                                            <div class="stats">
                                                <select class="form-control" name="priority">
                                                    <option value="Cao">Cao</option>
                                                    <option value="Thấp">Thấp</option>
                                                    <option value="Trung bình">Trung bình</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-10">
                                            <div class="form-group">
                                                <label>Triệu chứng</label>
                                                <textarea class="form-control" placeholder="Triệu chứng" name="symptom"></textarea>
                                            </div>
                                        </div>
                                    </div>

                                    <div id="dynamicStep">
                                        <div  class="row">
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label>Hướng dẫn bước 1</label>
                                                    <input type="text" class="form-control" placeholder="Hướng dẫn" name="instruction_step1">
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label>Giải thích</label>
                                                    <textarea class="form-control" placeholder="Giải thích" name="detail_step1"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                        <div  class="row">
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label>Hình ảnh</label>
                                                    <input type="file" class="form-control" name="image_step1">
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label>Âm thanh</label>
                                                    <input type="file" class="form-control" name="audio_step1">
                                                </div>
                                            </div>
                                        </div>  
                                    </div>
                                    <input type="hidden" id="count_step" name="count_step" value="">

                                    <div class="row">
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <input type="button" class="form-control" value="Thêm bước" onClick="addInput('dynamicStep');">
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <input type="submit" class="form-control btn btn-fill" value="Thêm chấn thương">
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
    <!-- add new input control handle -->
    <script type="text/javascript">
        var counter = 1;
        var limit = 10;
        document.getElementById('count_step').value = counter;
        function addInput(divName){
            if (counter == limit)  {
                alert("Bạn chỉ thêm được tối đa " + counter + " bước");
            }
            else {
                var newdiv = document.createElement('div');
                var instruction_step = "instruction_step" + (counter + 1);
                var detail_step = "detail_step" + (counter + 1);
                var image_step = "image_step" + (counter + 1);
                var audio_step = "audio_step" + (counter + 1);

                newdiv.innerHTML = 
                "<div  class='row'>" +
                    "<div class='col-md-5'>" +
                        "<div class='form-group'>" +
                            "<label>Hướng dẫn bước " + (counter + 1) + "</label>" +
                            "<input type='text' class='form-control' placeholder='Hướng dẫn' name='" + instruction_step+ "'>" +
                        "</div>" + 
                    "</div>" +
                    "<div class='col-md-5'>" +
                        "<div class='form-group'>" +
                            "<label>Giải thích</label>" +
                            "<textarea class='form-control' placeholder='Giải thích' name='" + detail_step + "'></textarea>" +
                        "</div>" +
                    "</div>" +
                "</div>" +
                "<div  class='row'>" +
                    "<div class='col-md-5'>" +
                        "<div class='form-group'>" +
                            "<label>Hình ảnh</label>" +
                            "<input type='file' class='form-control' name='" + image_step+ "'>" +
                        "</div>" +
                    "</div>" +
                    "<div class='col-md-5'>" +
                        "<div class='form-group'>" +
                            "<label>Âm thanh</label>" +
                            "<input type='file' class='form-control' name='" + audio_step+ "'>" +
                        "</div>" +
                    "</div>" +
               "</div>";
                // 
                document.getElementById(divName).appendChild(newdiv);
                counter++;
            }
            document.getElementById('count_step').value = counter;
        }
    </script>
@endsection