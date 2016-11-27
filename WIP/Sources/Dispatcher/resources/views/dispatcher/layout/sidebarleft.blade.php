<div class="sidebar" style="margin-right: 5px" data-color="azure">

	<!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


	<div class="sidebar-wrapper">
		<div class="logo">
			<a href="dispatcher.html" class="simple-text">
				Điều phối
			</a>
		</div>
		<form action="expert/injury/addinjury" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="_token" value="{{ csrf_token() }}"/>
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
					<!-- <div>
						<input type="text" class="form-control" name="injury" list="injury" placeholder="Injury...">       
					</div>
					<datalist id="injury">
						@if(isset($injury))
							@foreach($injury as $inj)
								<option>{{ $inj->injury_name }}</option>
							@endforeach
						@endif
					</datalist> -->
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

				<!-- caller location -->
				<input id="latitude" name="latitude" type="hidden">
				<input id="longitude" name="longitude" type="hidden">

				<li style="padding: 10px;">
					<div style="align-content: center;">
					<button style="background-color: green; color: white; float: right; width: 100px" type="submit" class="btn btn-default">Điều phối xe</button>
						<button style="background-color: red; color: white; float: right; width: 100px" type="reset" class="btn btn-default">Hủy</button>
					</div>
				</li>
			</ul>
		</form>

		

	</div>
</div>