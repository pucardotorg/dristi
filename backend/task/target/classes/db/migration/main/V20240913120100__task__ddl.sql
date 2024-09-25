CREATE INDEX IF NOT EXISTS idx_dristi_task_order_id ON dristi_task(orderId);
CREATE INDEX IF NOT EXISTS idx_dristi_task_cnr_number ON dristi_task(cnrNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_task_task_number ON dristi_task(taskNumber);
CREATE INDEX IF NOT EXISTS idx_dristi_task_status ON dristi_task(status);

CREATE INDEX IF NOT EXISTS idx_dristi_task_document_task_id ON dristi_task_document(task_id);

CREATE INDEX IF NOT EXISTS idx_dristi_task_amount_task_id ON dristi_task_amount(task_id);