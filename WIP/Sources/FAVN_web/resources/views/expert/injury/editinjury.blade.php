@extends('expert.layout.index')

@section('content')
<div class="content">
	<div class="card">
		<div class="header">
			<h4 class="title">
				Sửa chấn thương : {{ $injury->injury_name }}
			</h4>
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

			<form action="expert/injury/editinjury/{{ $injury->id }}" method="POST" enctype="multipart/form-data">
				<input type="hidden" name="_token" value="{{ csrf_token() }}"/>
				
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label>Tên chấn thương</label>
							<input type="text" class="form-control" placeholder="Chấn thương..." name="injury_name" value="{{ $injury->injury_name }}">
						</div>
					</div>
					<div class="col-md-2">
						<label>Ưu tiên</label>
						<div class="stats">
							<select class="form-control" name="priority">
								<option value="Cao"
									@if($injury->priority == 'Cao')
									selected
									@endif
									>
									Cao
								</option>

								<option value="Thấp"
									@if($injury->priority == 'Thấp')
									selected
									@endif
									>
									Thấp
								</option>

								<option value="Trung bình"
									@if($injury->priority == 'Trung Bình')
									selected
									@endif
									>
									Trung bình
								</option>
							</select>
						</div>		
					</div>
				</div>

				<div class="row">
					<div class="col-md-8">
						<div class="form-group">
							<label>Triệu chứng</label>
							<textarea class="form-control" placeholder="Triệu chứng" name="symptom">{{ $injury->symptom }}</textarea>
						</div>
					</div>
				</div>


				<hr>
				<!-- START Emergency instructions -->
				<label>Hướng dẫn bước sơ cứu</label>
				<div id="dynamicStepEmergency">
				@foreach($injury->instruction as $ins)
					<div  class="row">
						<div class="col-md-5">
							<div class="form-group">
								<label>Hướng dẫn bước {{ $ins->step }}</label>
								<textarea type="text" class="form-control" placeholder="Hướng dẫn" name="emergency_instruction_step{{ $loop->iteration }}">{{ $ins->content }}</textarea>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<br>
								@if($ins->isMakeCall == '1')
								<input type="checkbox" id="cbIsCall{{ $loop->iteration }}" name="isCall1" style="margin-bottom: 20px;" checked> Là bước gọi cấp cứu 115
								@else
								<input type="checkbox" id="cbIsCall{{ $loop->iteration }}" name="isCall1" style="margin-bottom: 20px;"> Là bước gọi cấp cứu 115
								@endif

							</div>
						</div>
						<div class="col-md-2">
							<div>
								<label>Hình ảnh</label>
								<input type="file" name="emergency_image_step{{ $loop->iteration }}">
							</div>
							<div>
								<label>Âm thanh</label>
								<input type="file" name="emergency_audio_step{{ $loop->iteration }}">
							</div>
						</div>
					</div>
				@endforeach
				</div> 
				<!-- EMD Emergency instructions -->

				<hr>

				<!-- START Emergency learning instrcution -->
				<label>Hướng dẫn bước học</label>
				<div id="dynamicStepLearn">
				@foreach($injury->learningInstruction as $ins)
					<div  class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label>Hướng dẫn bước 1</label>
								<textarea type="text" class="form-control" placeholder="Hướng dẫn" name="learning_instruction_step{{ $loop->iteration }}">{{ $ins->content }}</textarea>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>Giải thích</label>
								<textarea class="form-control" id='taDetail_step1' placeholder="Giải thích" name="learning_detail_step{{ $loop->iteration }}">{{ $ins->explain }}</textarea>
							</div>
						</div>
						<div class="col-md-2">
							<div>
								<label>Hình ảnh</label>
								<input type="file" name="learning_image_step{{ $loop->iteration }}">
							</div>
							<div>
								<label>Âm thanh</label>
								<input type="file" name="learning_audio_step{{ $loop->iteration }}">
							</div>
						</div>
					</div>
				@endforeach
				</div> 
				<!-- EMD Emergency learning instruction -->

				<hr>


				<input type="hidden" id="count_step_learning" name="count_step_learning" value="">

				<input type="hidden" id="count_step_emergence" name="count_step_emergence" value="">

				<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<input type="submit" class="form-control btn btn-fill" value="Sửa chấn thương">
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
@endsection

@section('script')

<!-- add new input control handle -->
<script type="text/javascript">
    var counterLearning =  {{ $injury->learningInstruction->count() }};
    var counterEmergency = {{ $injury->instruction->count() }};
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

@endsection



