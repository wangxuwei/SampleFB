var app = app || {};


// --------- Entity Dao Registration --------- //
(function($){
		//register RemoteDao
		brite.registerDao(new app.RemoteGroupDao());
		brite.registerDao(new app.RemoteContactDao());
})();

