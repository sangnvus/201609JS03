<div class="sidebar" style="margin-right: 5px" data-color="azure">

	<!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


	<div class="sidebar-wrapper">
		<div class="container">
			<h3>Điều xe</h3>
		</div>

		<ul class="nav">
			<form id="form_caller" action="dispatch" method="POST" enctype="multipart/form-data">
				<input type="hidden" name="_token" value="{{ csrf_token() }}"/>
				<label style="padding-left: 10px;">Số điện thoại</label>
				<li style="padding: 10px;">
					<div>
						<input id="phone" name="phone" type="text" class="form-control" placeholder="Số điện thoại" value="{{ old('phone') }}">
					</div>
				</li>
				<label style="padding-left: 10px;">Chấn thương</label>
				<li style="padding: 10px;">
					<select id="injury_id" class="form-control" name="injury">
						<option value="0">Khác</option>
						@if(isset($injury))
							@foreach($injury as $inj)
								@if(old('injury') == $inj->id)
									<option value="{{ $inj->id }}" selected>{{ $inj->injury_name }}</option>
								@else
									<option value="{{ $inj->id }}">{{ $inj->injury_name }}</option>
								@endif
							@endforeach
						@endif
					</select>
				</li>

				<label style="padding-left: 10px;">Triệu chứng khác</label>
				<li style="padding: 10px;">
					<div>
						<textarea id="symptom" name="symptom" type="text" class="form-control" placeholder="Triệu chứng khác">{{ old('symptom') }}</textarea>
					</div>
				</li>

				<label style="padding-left: 10px;">Vị trí người gọi</label>
				<li style="padding: 10px;">
					<div>
						<textarea id="address" name="address" type="text" class="form-control" placeholder="Vị trí người gọi">{{ old('address') }}</textarea>
					</div>
				</li>

				<!-- caller location -->
				<input id="latitude" name="latitude" type="hidden" value="{{ old('latitude') }}">
				<input id="longitude" name="longitude" type="hidden" value="{{ old('longitude') }}">
				<input id="caller_id" name="caller_id" type="hidden" value="{{ old('caller_id') }}">
				<input id="dispatcher_user_id" name="dispatcher_user_id" type="hidden" value="{{ $userLogin->id }}">
				<li style="padding: 10px;">
					<div style="align-content: center;">
						<button style="background-color: green; color: white; float: right; width: 100px" type="button" class="btn btn-default" onclick="onDispatchClick()">Điều xe</button>
						<button style="background-color: red; color: white; float: right; width: 100px" type="button" class="btn btn-default" onclick="onCancelDispatchClick()">Hủy</button>
					</div>
					<br>
				</li>

							

			</form>

		</ul>

		<div id="ambulanceInfoBox" style="display: none;">
			<ul class="nav">
				<li style="padding: 10px;">
					<hr>
					<label>Thông tin xe cứu thương</label>
					<a id="aAmbulanceInfoBox" style="background-color: white; margin: 0px;" href="javascript:void(0)" onclick="">	
						<p class="ambulance-infobox-row" id="infobox_team" style="display: none;"></p>
						
						<p class="ambulance-infobox-row" id="infobox_status" style="display: none;"></p>
						
						<p class="ambulance-infobox-row" id="inforbox_caller" style="display: none;"></p>
						
						<p class="ambulance-infobox-row" id="inforbox_distance" style="display: none;"></p>

						<p class="ambulance-infobox-row" id="inforbox_duration" style="display: none;"></p>
					</a>
				</li>
			</ul>
		</div>

	</div>
</div>
