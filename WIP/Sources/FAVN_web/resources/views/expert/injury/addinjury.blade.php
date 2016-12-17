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
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>Tên chấn thương</label>
                                <input type="text" class="form-control" placeholder="Chấn thương..." name="injury_name" value="{{ old('injury_name') }}">
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
                        <div class="col-md-2">
                            <div>
                                <label>Hình ảnh</label>
                                <input type="file" name="injury_image">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label>Triệu chứng</label>
                                <textarea class="form-control" placeholder="Triệu chứng" name="symptom"></textarea>
                            </div>
                        </div>
                    </div>

                    <hr>

                    <!-- TODO -->
                    <div id="dynamicStepEmergency">
                        <label>Hướng dẫn bước sơ cứu</label>
                        <div  class="row">
                            <div class="col-md-5">
                                <div class="form-group">
                                    <label>Hướng dẫn bước 1</label>
                                    <textarea type="text" class="form-control" placeholder="Hướng dẫn" name="emergency_instruction_step1"></textarea>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                <br>
                                    <input type="checkbox" id="cbIsCall1" name="isCall1" style="margin-bottom: 20px;"> Là bước gọi cấp cứu 115
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div>
                                    <label>Hình ảnh</label>
                                    <input type="file" name="emergency_image_step1">
                                </div>
                                <div>
                                    <label>Âm thanh</label>
                                    <input type="file" name="emergency_audio_step1">
                                </div>
                            </div>
                        </div>
                        
                    </div> 
                    <!-- EMD TODO -->

                    <hr>

                    <div id="dynamicStepLearn">
                        <label>Hướng dẫn bước học</label>
                        <div  class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label>Hướng dẫn bước 1</label>
                                    <textarea type="text" class="form-control" placeholder="Hướng dẫn" name="learning_instruction_step1"></textarea>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label>Giải thích</label>
                                    <textarea class="form-control" id='taDetail_step1' placeholder="Giải thích" name="learning_detail_step1"></textarea>
                                </div>
                            </div>
                            <div class="col-md-2">
                                <div>
                                    <label>Hình ảnh</label>
                                    <input type="file" name="learning_image_step1">
                                </div>
                                <div>
                                    <label>Âm thanh</label>
                                    <input type="file" name="learning_audio_step1">
                                </div>
                            </div>
                        </div>
                    </div> 

                    <hr>

                    <input type="hidden" id="count_step_learning" name="count_step_learning" value="">

                     <input type="hidden" id="count_step_emergence" name="count_step_emergence" value="">

                    <div class="row">
                        <div class="col-md-2">
                            <div class="form-group">
                                <input type="button" class="form-control" value="Thêm bước sơ cứu" onClick="addInputEmergency('dynamicStepEmergency');">
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="form-group">
                                <input type="button" class="form-control" value="Thêm bước học" onClick="addInputLearn('dynamicStepLearn');">
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
    var counterLearning = 1; 
    var counterEmergency = 1; 
    var limit = 10;
    document.getElementById('count_step_emergence').value = counterEmergency;
    document.getElementById('count_step_learning').value = counterLearning;

    function addInputLearn(divName){
        if (counterLearning == limit)  {
            alert("Bạn chỉ thêm được tối đa " + counterLearning + " bước");
        }
        else {
            var newdiv = document.createElement('div');
            var learning_instruction_step = "learning_instruction_step" + (counterLearning + 1);
            var learning_detail_step = "learning_detail_step" + (counterLearning + 1);
            var learning_image_step = "learning_image_step" + (counterLearning + 1);
            var learning_audio_step = "learning_audio_step" + (counterLearning + 1);

            newdiv.innerHTML = 
            "<div  class='row'>" +
            "<div class='col-md-4'>" +
            "<div class='form-group'>" +
            "<label>Hướng dẫn bước " + (counterLearning + 1) + "</label>" +
            "<textarea type='text' class='form-control' placeholder='Hướng dẫn' name='" + learning_instruction_step+ "'></textarea>" +
            "</div>" + 
            "</div>" +
            "<div class='col-md-4'>" +
            "<div class='form-group'>" +
            "<label>Giải thích</label>" +
            "<textarea class='form-control' placeholder='Giải thích' name='" + learning_detail_step + "'></textarea>" +
            "</div>" +
            "</div>" +
            "<div class='col-md-2'>" +
            "<div>" +
            "<label>Hình ảnh</label>" +
            "<input type='file' name='" + learning_image_step+ "'>" +
            "</div>" +
            "<div>" +
            "<label>Âm thanh</label>" +
            "<input type='file' name='" + learning_audio_step+ "'>" +
            "</div>" +
            "</div>" +
            "</div>";
                // 
                document.getElementById(divName).appendChild(newdiv);
                counterLearning++;
            }
            document.getElementById('count_step_learning').value = counterLearning;
    }
    

    function addInputEmergency(divName){
        if (counterEmergency == limit)  {
            alert("Bạn chỉ thêm được tối đa " + counterEmergency + " bước");
        }
        else {
            var newdiv = document.createElement('div');
            var emergency_instruction_step = "emergency_instruction_step" + (counterEmergency + 1);
            var emergency_image_step = "emergency_image_step" + (counterEmergency + 1);
            var emergency_audio_step = "emergency_audio_step" + (counterEmergency + 1);
            var isCall = "isCall" + (counterEmergency + 1);

            newdiv.innerHTML = 
            "<div  class='row'>" +
            "<div class='col-md-5'>" +
            "<div class='form-group'>" +
            "<label>Hướng dẫn bước " + (counterEmergency + 1) + "</label>" +
            "<textarea type='text' class='form-control' placeholder='Hướng dẫn' name='" + emergency_instruction_step+ "'></textarea>" +
            "</div>" + 
            "</div>" +
            "<div class='col-md-3'>" +
            "<div class='form-group'>" +
            "<br>" +
            "<input type='checkbox' name='" + isCall + "' style='margin-bottom: 20px;'> Là bước gọi cấp cứu 115" +
            "</div>" +
            "</div>" +
            "<div class='col-md-2'>" +
            "<div>" +
            "<label>Hình ảnh</label>" +
            "<input type='file' name='" + emergency_image_step+ "'>" +
            "</div>" +
            "<div>" +
            "<label>Âm thanh</label>" +
            "<input type='file' name='" + emergency_audio_step+ "'>" +
            "</div>" +
            "</div>" +
            "</div>";
                // 
                document.getElementById(divName).appendChild(newdiv);
                counterEmergency++;
            }
            document.getElementById('count_step_emergence').value = counterEmergency;
           
    }
    

        
        
    </script>

    <!-- TODO -->
    <script type="text/javascript">

        function readURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    $('#blah')
                    .attr('src', e.target.result)
                    .width(150)
                    .height(200);
                };

                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>

    <!-- filterTypeInstruction on change value handle -->
    <script type="text/javascript">
        var selectEl = document.getElementById('filterTypeInstruction');
        selectEl.onchange = function(){
            for(i = 1; i <= counter; i++){
                var taDetail_step = "taDetail_step" + i;
                var cbIsCall = "cbIsCall" + i;
                if (selectEl.value == 'instruction') { 
                    document.getElementById(taDetail_step).disabled = true;
                    document.getElementById(cbIsCall).disabled = false;
                } else {
                    document.getElementById(taDetail_step).disabled = false;
                    document.getElementById(cbIsCall).disabled = true;
                }
            }
        };
    </script>

    @endsection