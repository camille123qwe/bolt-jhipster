import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/app1/customer.model';
import { ProjectRole } from 'app/shared/model/enumerations/project-role.model';

export interface IProject {
  id?: number;
  uuid?: string;
  name?: string;
  label?: string | null;
  description?: string | null;
  role?: keyof typeof ProjectRole | null;
  createdAt?: dayjs.Dayjs;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IProject> = {};
