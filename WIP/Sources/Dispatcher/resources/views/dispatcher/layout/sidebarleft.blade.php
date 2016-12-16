<div class="sidebar" style="margin-right: 5px" data-color="azure">

	<!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


	<div class="sidebar-wrapper">
	<div class="container">
	<h3>Điều xe</h3>
	</div>
		
			<ul class="nav">
			
				<label style="padding-left: 10px;">Số điện thoại</label>
				<li style="padding: 10px;">
					<div>
						<input id="phone" name="phone" type="text" class="form-control" placeholder="Số điện thoại">
					</div>
				</li>

				<label style="padding-left: 10px;">Chấn thương</label>
				<li style="padding: 10px;">
					<select id="injury_id" class="form-control">
						<option value="0">Khác</option>
						@if(isset($injury))
							@foreach($injury as $inj)
								<option value="{{ $inj->id }}">{{ $inj->injury_name }}</option>
							@endforeach
						@endif
					</select>
				</li>

				<label style="padding-left: 10px;">Triệu chứng khác</label>
				<li style="padding: 10px;">
					<div>
						<textarea id="symptom" name="symptom" type="text" class="form-control" placeholder="Triệu chứng khác"></textarea>
					</div>
				</li>

				<label style="padding-left: 10px;">Vị trí người gọi</label>
				<li style="padding: 10px;">
					<div>
					<textarea id="address" name="address" type="text" class="form-control" placeholder="Vị trí người gọi"></textarea>
					</div>
				</li>

				<input id="status" name="status" type="hidden">
				<!-- caller location -->
				<input id="latitude" name="latitude" type="hidden">
				<input id="longitude" name="longitude" type="hidden">
				<form id="form_caller" action="dispatch" method="POST" enctype="multipart/form-data">
						<input type="hidden" name="_token" value="{{ csrf_token() }}"/>
						<input id="caller_id" name="caller_id" type="hidden">
						<li style="padding: 10px;">
							<div style="align-content: center;">
							<button style="background-color: green; color: white; float: right; width: 100px" type="button" class="btn btn-default" onclick="onDispatchClick()">Điều xe</button>
								<button style="background-color: red; color: white; float: right; width: 100px" type="button" class="btn btn-default" onclick="onCancelDispatchClick()">Hủy</button>
							</div>
						</li>
					
				</form>

		</ul>

	</div>
</div>
