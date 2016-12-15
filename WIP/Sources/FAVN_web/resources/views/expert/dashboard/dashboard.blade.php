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
	                            Thống kê cuộc gọi
	                        </h4>
	                       <!--  <p class="category">Danh sách câu hỏi / ý kiến</p> -->
	                        <div class="row">
	                           <!--  <div class="col-md-3">
	                                <div class="stats">
	                                	@if(isset($filtertype))
		                                    <select id="filterQuestion" class="form-control" name="cars">
		                                        <option value="all"
		                                        	@if($filtertype == 'all')
														selected
													@endif
		                                        >Tất cả</option>
		                                        <option value="not_answered"
		                                        @if($filtertype == 'not_answered')
		                                        	selected
		                                        @endif
		                                        >Chưa trả lời</option>
		                                        <option value="answered"
		                                        @if($filtertype == 'answered')
		                                        	selected
		                                        @endif
		                                        >Đã trả lời</option>
		                                    </select>
	                                    @else
		                                    <select id="filterQuestion" class="form-control" name="cars">
		                                        <option value="all" selected>Tất cả</option>
		                                        <option value="not_answered">Chưa trả lời</option>
		                                        <option value="answered">Đã trả lời</option>
		                                    </select>
	                                    @endif
	                                </div>
	                            </div> -->
	                            <!-- <div class="col-md-9">
	                            	<ul class="nav nav-pills">
	                            		<li class="active"><a href="#">Thống kê cuộc gọi</a></li>
	                            	</ul>
	                            </div> -->
	                            <div class="col-md-12">
									<form action="expert/qanda/search" class="form-inline pull-right">
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

							<div class="statistical-bar" id="test">
									THỐNG KÊ - Câu hỏi về chấn thương: 10 | Không nối được xe : 3 | Cuộc gọi mục đích khác : 2 
							</div>
							<table class="table table-hover table-striped">
								<thead>
									<th>Số điện thoại</th>
									<th>Chấn thương</th>
									<th>Thời gian gọi</th>
									<th>Tình trạng</th>
									<th>Thao tác</th>
								</thead>
								<tbody>
									
										<tr>
										<td>
											0906 113 555
										</td>
										<td>
											Gãy chân
										</td>
										<td>
											8:39 PM - 12/12/2016
										</td>
										<td>
											Đã được nối xe
										</td>

										<td style="width: 85px;">
											<button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs">
												<i class="fa fa-search-plus fa-lg"></i>
											</button>

											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs">
												<i class="fa fa-times fa-lg"></i>
											</button>
										</td>

									</tr>

									<tr>
										<td>
											0305 118 239
										</td>
										<td>
											Bỏng nặng
										</td>
										<td>
											8:39 PM - 12/12/2016
										</td>
										<td>
											Hủy nối xe
										</td>

										<td style="width: 85px;">
											<button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs">
												<i class="fa fa-search-plus fa-lg"></i>
											</button>

											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs">
												<i class="fa fa-times fa-lg"></i>
											</button>
										</td>

									</tr>

									<tr>
										<td>
											0906 113 555
										</td>
										<td>
											Gãy chân
										</td>
										<td>
											8:39 PM - 12/12/2016
										</td>
										<td>
											Đã được nối xe
										</td>

										<td style="width: 85px;">
											<button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs">
												<i class="fa fa-search-plus fa-lg"></i>
											</button>

											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs">
												<i class="fa fa-times fa-lg"></i>
											</button>
										</td>

									</tr>

									<tr>
										<td>
											0123 252 213
										</td>
										<td>
											Đau ruột thừa
										</td>
										<td>
											8:39 PM - 12/12/2016
										</td>
										<td>
											Đã được nối xe
										</td>

										<td style="width: 85px;">
											<button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs">
												<i class="fa fa-search-plus fa-lg"></i>
											</button>

											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs">
												<i class="fa fa-times fa-lg"></i>
											</button>
										</td>

									</tr>

									<tr>
										<td>
											0906 113 555
										</td>
										<td>
											Hen xuyễn
										</td>
										<td>
											8:39 PM - 12/12/2016
										</td>
										<td>
											Đã được nối xe
										</td>

										<td style="width: 85px;">
											<button type="button" rel="tooltip" title="Reply" class="btn btn-info btn-simple btn-xs">
												<i class="fa fa-search-plus fa-lg"></i>
											</button>

											<button type="button" rel="tooltip" title="Remove" class="btn btn-danger btn-simple btn-xs">
												<i class="fa fa-times fa-lg"></i>
											</button>
										</td>

									</tr>
								</tbody>
							</table>
						</div>

	                   
	                </div>
	                <!--End table injuries-->

	            </div>
	        </div>
	    </div>
	</div>
@endsection

@section('script')

	<!-- JS function confirm box when delete injury -->
	<script type="text/javascript">

		function callDelete(question_id){
			bootbox.confirm({ 
				size: "small",
				message: "Bạn có muốn xóa Câu hỏi này ?", 
				callback: function(result){
					if (result) {
						window.location = 'admin/qanda/deletequestion/' + question_id;
					}
				}
			})
		}

	</script>

	<!-- set class active for li (menu list item) -->
	<script type="text/javascript">
	    document.getElementById("nav-dashboard-item").className += "active";
	</script>


	<!-- Fillter question by dropdown list -->
	<script type="text/javascript">
		function redirect(goto){
			if (goto != '') {
				window.location = goto;
			}
		}

		var selectEl = document.getElementById('filterQuestion');

		selectEl.onchange = function(){
			var goto = 'expert/qanda/listqanda/' + this.value;
			redirect(goto);
		};
	</script>
	
@endsection