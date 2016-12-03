<div class="sidebar" style="margin-left: 5px;" data-color="azure">
	<div class="sidebar-wrapper">
		<div class="container">
			<h3>Trường hợp cấp cứu</h3>
			<ul class="nav nav-tabs">
				<li id="tabWaiting">
					<a href="javascript:void(0)" onclick="getCallers(event, 'waiting')">
						Đội cứu thương
					</a>
				</li>
				<li id="tabWaiting">
					<a href="javascript:void(0)" onclick="getCallers(event, 'waiting')">
						Đang đợi
					</a>
				</li>
				<li id="tabProcessing">
					<a href="javascript:void(0)" onclick="getCallers(event, 'processing')">
						Đang xử lý
					</a>
				</li>
				
			</ul>
		</div>
		<div>
			<!-- <ul class="nav" id="listAmbulance">
				@if(isset($ambulances))
					@foreach($ambulances as $ambulance)
						<li id="liAmbulance{{ $ambulance->id }}" class=" active">
							<a id="{{ $ambulance->id }}" href="javascript:void(0)" onclick="onClickLiAmbulance({{ $ambulance->id }});">	
								<p id="pAmbulanceTeam{{ $ambulance->id }}">{{ $ambulance->team }}</p>
								<p id="pAmbulanceLatitude{{ $ambulance->id }}">{{ $ambulance->latitude }}</p>
								<p id="pAmbulanceLongitude{{ $ambulance->id }}">{{ $ambulance->longitude }}</p>
								<p id="pAmbulanceStatus{{ $ambulance->id }}">{{ $ambulance->status }}</p>
							</a>
						</li>
					@endforeach
				@endif
			</ul> -->


			<ul class="nav" id="listAmbulance"> 
			
			</ul> 

			<!-- <ul class="nav" id="listWaiting"> -->
				<!-- content go here -->
			<!-- </ul> -->
			<!-- <ul class="nav" id="listProcessing"> -->
				<!-- content go here -->
			<!-- </ul> -->
		</div>
	</div>
</div>





