import dayjs from 'dayjs';
import { IExecutionType } from 'app/shared/model/app1/execution-type.model';
import { ISource } from 'app/shared/model/app1/source.model';
import { IProject } from 'app/shared/model/app1/project.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IStrategy {
  id?: number;
  uuid?: string;
  label?: string;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
  executionRule?: string;
  status?: keyof typeof Status;
  executionType?: IExecutionType | null;
  source?: ISource | null;
  project?: IProject | null;
}

export const defaultValue: Readonly<IStrategy> = {};
