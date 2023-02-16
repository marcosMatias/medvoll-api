ALTER TABLE medicos  DROP COLUMN ativo;

alter table medicos add ativo  boolean;
		
update medicos set ativo =TRUE;

