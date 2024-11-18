import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './project.reducer';

export const ProjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const projectEntity = useAppSelector(state => state.app1.project.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="projectDetailsHeading">
          <Translate contentKey="app1App.app1Project.detail.title">Project</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="app1App.app1Project.id">Id</Translate>
            </span>
          </dt>
          <dd>{projectEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="app1App.app1Project.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{projectEntity.uuid}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="app1App.app1Project.name">Name</Translate>
            </span>
          </dt>
          <dd>{projectEntity.name}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="app1App.app1Project.label">Label</Translate>
            </span>
          </dt>
          <dd>{projectEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="app1App.app1Project.description">Description</Translate>
            </span>
          </dt>
          <dd>{projectEntity.description}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="app1App.app1Project.role">Role</Translate>
            </span>
          </dt>
          <dd>{projectEntity.role}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="app1App.app1Project.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{projectEntity.createdAt ? <TextFormat value={projectEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="app1App.app1Project.customer">Customer</Translate>
          </dt>
          <dd>{projectEntity.customer ? projectEntity.customer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/app1/project" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app1/project/${projectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProjectDetail;
