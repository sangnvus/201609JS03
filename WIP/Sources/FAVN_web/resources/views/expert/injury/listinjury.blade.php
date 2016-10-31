@extends('expert.layout.index')

@section('content')
	<div class="content">
		<div class="container-fluid">
			<div class="row">

				<!--Table of injuries-->
				<div class="col-md-12">
					<div class="card">

						<div class="header">
							<h4 class="title">
								Chấn thương
								<a href="expert/injury/addinjury" class="btn btn-info btn-fill">Thêm chấn thương</a>
							</h4>
							<div class="row">
								<div class="col-md-12">
									<form action="expert/injury/search" class="form-inline pull-right">
										<div class="form-group">
											<input type="text" class="form-control" id="search" placeholder="Search" name="term">
										</div>
										<button type="submit" class="btn btn-default btn-fill">Tìm kiếm</button>
									</form>
								</div>
							</div>
						</div>

						<div class="content table-responsive table-full-width">

							@if(session('noti'))
								<div class="alert alert-success">
									{{ session('noti') }}
								</div>
							@endif

							<table class="table table-hover table-striped">
								<thead>
									<th>Tên chấn thương</th>
									<th>Triệu chứng</th>
									<th>Hướng dẫn sơ cứu</th>
									<th>Ưu tiên</th>
									<th>Thao tác</th>
								</thead>
								<tbody>
									<!-- show list of injury -->
									@foreach($injury as $inj) 
									<tr>
										<td>{{ $inj->injury_name }}</td> 
										
										<td>{{ $inj->symptom }}</td>
							
										<td>
											@foreach($inj->instruction as $ins)
												<span style="color: green; font-weight: bold;">Step {{ $ins->step }} :</span> {{ $ins->instruction }}<br/>
											@endforeach
										</td>
									
										<td>{{ $inj->priority }}</td>
										<td style="width: 85px;">
											<button type="button" rel="tooltip" title="Edit Injury" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='expert/injury/editinjury/{{ $inj->id }}'">
												<i class="fa fa-edit fa-lg"></i>
											</button>

											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="callDelete({{ $inj->id }})">
												<i class="fa fa-times fa-lg"></i>
											</button>
										</td>
									</tr>
									@endforeach 
									<!-- end show list of injury -->
								</tbody>
							</table>
							<!-- paging -->
							<center><?php echo $injury->render(); ?></center>
						</div>
					</div>
				</div>
				<!--End table injuries-->
			</div>
		</div>
	</div>
@endsection

@section('script')

	<!-- JS function confirm box when delete injury -->
	<script type="text/javascript">

		function callDelete(injury_id){
			bootbox.confirm({ 
				size: "small",
				message: "Bạn có muốn xóa chấn thương này ?", 
				callback: function(result){
					if (result) {
						window.location = 'expert/injury/deleteinjury/' + injury_id;
					}
				}
			})
		}

	</script>

	<!-- set class active for li (menu list item) -->
	<script type="text/javascript">
	    document.getElementById("nav-injuries-item").className += "active";
	</script>

@endsection
