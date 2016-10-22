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
							<p class="category">Danh sách chấn thương</p>
							<div class="row">
								<div class="col-md-3">
									<div class="stats">
										<select class="form-control" name="cars">
											<option value="all">Tất cả</option>
											<option value="season">Mùa</option>
											<option value="hear">...</option>
											<option value="bone">...</option>
										</select>
									</div>
								</div>
								<div class="col-md-9">
									<form class="form-inline pull-right">
										<div class="form-group">
											<input type="password" class="form-control" id="search" placeholder="Search">
										</div>
										<button type="submit" class="btn btn-default btn-fill">Search</button>
									</form>
								</div>
							</div>
						</div>

						<div class="content table-responsive table-full-width">
							<table class="table table-hover table-striped">
								<thead>
									<th></th>
									<th>Tên chấn thương</th>
									<th>Triệu chứng</th>
									<th>Hướng dẫn</th>
									<th>Ưu tiên</th>
									<th>Thao tác</th>
								</thead>
								<tbody>
									<!-- show list of injury -->
									@foreach($injury as $inj) 
									<tr onclick="selectRow(this)">
										<td>{{ $inj->injury_name }}</td> 
										
										<td>{{ $inj->symptom }}</td>
							
										<td>
											@foreach($inj->instruction as $ins)
												<span style="color: green; font-weight: bold;">Step {{ $ins->step }} :</span> {{ $ins->instruction }}<br/>
											@endforeach
										</td>
									
										<td>{{ $inj->reference }}</td>
										<td>{{ $inj->priority }}</td>
										<td class="td-actions text-right" style="white-space:nowrap">
											<button type="button" rel="tooltip" title="View" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='viewinjury.html'">
												<i class="fa fa-picture-o"></i>
											</button>
											<button type="button" rel="tooltip" title="Edit Injury" class="btn btn-info btn-simple btn-xs" onclick="window.location.href='editinjury.html'">
												<i class="fa fa-edit"></i>
											</button>
											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs" onclick="DeleteRow(this)">
												<i class="fa fa-times"></i>
											</button>
										</td>
									</tr>
									@endforeach 
									<!-- end show list of injury -->
								</tbody>
							</table>

						</div>
					</div>
				</div>
				<!--End table injuries-->
				
			</div>
		</div>
	</div>
@endsection
