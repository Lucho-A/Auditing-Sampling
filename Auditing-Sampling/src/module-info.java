module AuditingSampling {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	
	opens auditingsampling to javafx.graphics, javafx.fxml;
}
